package com.xdtech.patent.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xdtech.parser.ParseResult;
import com.xdtech.parser.PatentParser;
import com.xdtech.patent.model.GraphModel;
import com.xdtech.search.client.LOGIC;
import com.xdtech.search.client.QueryObjectBuilder;
import com.xdtech.search.client.ws.BooleanQuery;
import com.xdtech.search.client.ws.FacetsResult;
import com.xdtech.search.client.ws.PivotFacetResult;
import com.xdtech.search.client.ws.PivotResultSet;
import com.xdtech.search.client.ws.QueryClause;
import com.xdtech.search.client.ws.SearchResult;

/**
 * 专利分析
 * 
 * @author changfei
 *
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class AnalysisAction extends AbstractBaseSeachAction {

	private static final long serialVersionUID = 4516858430992428312L;

	/**
	 * 分析逻辑
	 * 
	 * @param form
	 * @return
	 */
	protected ModelAndView doAnalysis(AnalysisForm form) {
		ModelAndView mv = new ModelAndView();
		QueryObjectBuilder builder = QueryObjectBuilder.newInstance();
		setupCore(form, builder);
		setupQuery(form, mv, builder);
		setupFilter(form, mv, builder);
		setupGraphFacet(form, builder);
		builder.addReturnField("docNo", false, null);
		builder.setPage(0, 1); // 分析时不用返回结果
		SearchResult result = search(builder);
		mv.addObject("search_result", result);
		return mv;
	}

	@Override
	public void setupQuery(SearchForm model, ModelAndView mv, QueryObjectBuilder builder) {
		String qstr = model.getKwd();
		if (StringUtils.isEmpty(qstr)) {
			// 专利分析菜单进入
			if (!StringUtils.isEmpty(model.gettName()) && !"all".equals(model.gettName())) {
				QueryClause T_NAME = newQueryClause(newTermQuery("T_NAME", model.gettName() + ""), LOGIC.AND);
				builder.addFilter(newBooleanQ(T_NAME));
			}
			builder.setQuery(newBooleanQ(newQueryClause(newTermQuery("UID", getSearchId()), LOGIC.AND)));
			mv.addObject("tip_title", "当前专题库");
		} else {
			// 专利检索结果页面或者专利分析按钮
			if (!kwdIsQuery(qstr)) {
				qstr = defaultQuery(qstr);
			}
			mv.addObject("tip_title", qstr);
			ParseResult result = PatentParser.parse(qstr, "TI");
			builder.setQuery(result.getQuery());
		}
	}

	@Override
	protected void setupFilter(SearchForm model, ModelAndView mv, QueryObjectBuilder builder) {
		super.setupFilter(model, mv, builder);
		if (null != model.getRs() && model.getRs().size() > 0) {
			for (RangeSearch rs : model.getRs()) {
				ParseResult result = PatentParser.parse(rs.toString(), "TI");
				BooleanQuery bq = result.getQuery();
				builder.addFilter(bq);
			}
		}
	}

	@Override
	protected void setupGraphFacet(AnalysisForm form, QueryObjectBuilder builder) {
		String xAxis = form.getXaxis();
		String[] field = form.getField();
		if (StringUtils.isNotEmpty(xAxis)) {
			// 柱状图 或折线图 或其他基于x-y坐标系类型的图
			if (ArrayUtils.isEmpty(field)) {
				if ("AD_Y_S".equals(xAxis) || "PD_Y_S".equals(xAxis)) {
					builder.addRangeFacet(xAxis, form.getStart() + "", form.getEnd() + "", form.getGap() + "");
				} else {
					builder.addFieldFacet(xAxis);
				}
			} else {
				// 二维统计，需要pivot(需要有xaxis 而且有field属性)
				builder.addPivotFacet(null, xAxis, form.getField()[0]);
				builder.addFacetParam("limit", field[0],Integer.MAX_VALUE+"");
			}
			if (form.getSort().equals("index")) {
				builder.addFacetParam("sort", xAxis, "index");
			}
			if (form.getLimit() > 0) {
				builder.addFacetParam("limit", xAxis, form.getLimit() + "");
			}
		} else {
			// 饼图 或其他非x-y坐标系图
			if (field != null && field.length == 1) {
				// 柱状图或折线图的表格数据，json类型数据
				if ("AD_Y_S".equals(field[0]) || "PD_Y_S".equals(field[0])) {
					builder.addRangeFacet(field[0], form.getStart() + "", form.getEnd() + "", form.getGap() + "");
				} else if ("LS".equals(field[0]) || "CNLX".equals(field[0]) || "PC".equals(field[0])) {
					builder.addFilter(newBooleanQ(newQueryClause(newTermQuery("PN", "CN*"), LOGIC.AND)));
					// builder.addQueryFacet("dd",newBooleanQ(newQueryClause(newTermQuery("AB",
					// "CN"), LOGIC.AND)));
					// builder.addQueryFacet("tt", "PN:CN*");
					builder.addFieldFacet(field[0]);
				} else {
					builder.addFieldFacet(field[0]);
				}
			} else {
				builder.addPivotFacet(null, field);
				builder.addFacetParam("limit", field[1],Integer.MAX_VALUE+"");
			}
			if (form.getSort().equals("index")) {
				builder.addFacetParam("sort", field[0], "index");
			}
			if (form.getLimit() > 0) {
				builder.addFacetParam("limit", field[0], form.getLimit() + "");
			}
		}

	}
	
	
	
	
	

	/**
	 * 自定义获取年份
	 * 
	 * @param length
	 * @return
	 */
	protected String[] getYear(int length) {
		int year = DateTime.now().getYear();
		String[] arr = new String[length];
		for (int i = 0, j = length; i < arr.length; i++, j--) {
			arr[i] = year - j + "";
		}
		return arr;
	}

	/**
	 * 拼接多个field字段数据
	 * 
	 * @param faceResult
	 * @param field
	 */
	protected void setMultiFieldData(FacetsResult faceResult, String[] field, GraphModel gm) {
		List categories = Lists.newArrayList();
		List data = Lists.newArrayList();
		PivotResultSet prs = faceResult.getPivotFacets().get(0);
		int size = prs.getResultSet().size() > 10 ? 10 : prs.getResultSet().size();
		for (int i = 0; i < size; i++) {
			PivotFacetResult pfr = prs.getResultSet().get(i);
			String value = pfr.getValue();
			if ("AC".equals(field[0])) {
				value = AC_CODE_MAP.get(value);
			}
			categories.add(value);
			int length = pfr.getPivots().size() > 20 ? 20 : pfr.getPivots().size();
			List<Map<String, String>> datas = Lists.newArrayList();
			for (int j = 0; j < length; j++) {
				Map<String, String> map = Maps.newHashMap();
				PivotFacetResult pr = pfr.getPivots().get(j);
				String val = pr.getValue();
				if ("AC".equals(field[1])) {
					val = AC_CODE_MAP.get(val) == null ? "未知" : AC_CODE_MAP.get(val);
				}
				map.put(val, pr.getCount() + "");
				datas.add(map);
			}
			data.add(datas);
		}
		gm.setCategories(categories);
		gm.setData(data);
	}

	/**
	 * 拼接折线图数据结构
	 * 
	 * @param arr
	 * @param gm
	 * @param faceResult
	 * @param xAxis
	 */
	protected void setBrokenGraphData(String[] arr, GraphModel gm, FacetsResult faceResult, String xAxis) {
		List categories = Arrays.asList(arr);
		List data = Lists.newArrayList();
		PivotResultSet prs = faceResult.getPivotFacets().get(0);
		List name = Lists.newArrayList();
		int size = prs.getResultSet().size() > 10 ? 10 : prs.getResultSet().size();
		for (int i = 0; i < size; i++) {
			PivotFacetResult pfr = prs.getResultSet().get(i);
			String value = pfr.getValue();
			if ("AC".equals(xAxis)) {
				value = AC_CODE_MAP.get(value);
			}
			name.add(value);
			Map<String, String> map = Maps.newHashMap();
			for (int j = 0; j < pfr.getPivots().size(); j++) {
				PivotFacetResult pr = pfr.getPivots().get(j);
				map.put(pr.getValue(), pr.getCount() + "");
			}
			List list = matchData(arr, map);
			data.add(list);
		}
		gm.setCategories(categories);
		gm.setData(data);
		gm.setName(name);
	}

	/**
	 * 拼接柱状图数据结构
	 * 
	 * @param arr
	 * @param gm
	 * @param faceResult
	 * @param xAxis
	 */
	protected void setBarGraphData(String[] arr, GraphModel gm, FacetsResult faceResult, String xAxis) {
		List name = Arrays.asList(arr);
		List data = Lists.newArrayList();
		List categories = Lists.newArrayList();

		PivotResultSet prs = faceResult.getPivotFacets().get(0);
		List<Map<String, String>> listMap = Lists.newArrayList();
		int size = prs.getResultSet().size() > 10 ? 10 : prs.getResultSet().size();
		for (int i = 0; i < size; i++) {
			PivotFacetResult pfr = prs.getResultSet().get(i);
			String value = pfr.getValue();
			if ("AC".equals(xAxis)) {
				value = AC_CODE_MAP.get(value);
			}
			categories.add(value);
			Map<String, String> map = Maps.newHashMap();
			for (int j = 0; j < pfr.getPivots().size(); j++) {
				PivotFacetResult pr = pfr.getPivots().get(j);
				map.put(pr.getValue(), pr.getCount() + "");
			}
			listMap.add(map);
		}
		for (int i = 0; i < arr.length; i++) {
			List list = Lists.newArrayList();
			for (Map<String, String> map : listMap) {
				if (map.containsKey(arr[i])) {
					list.add(Integer.valueOf(map.get(arr[i])));
				} else {
					list.add(0);
				}
			}
			data.add(list);
		}
		gm.setCategories(categories);
		gm.setData(data);
		gm.setName(name);
	}

	private List matchData(String[] arr, Map<String, String> map) {
		List data = Lists.newArrayList();
		for (int i = 0; i < arr.length; i++) {
			if (map.containsKey(arr[i])) {
				data.add(Integer.valueOf(map.get(arr[i])));
			} else {
				data.add(0);
			}
		}
		return data;
	}

	protected static Map<String, String> AC_CODE_MAP = new HashMap<String, String>();
	protected static String[] pc_code=new String[] {"北京","天津","上海","重庆","河北","山西","辽宁","吉林","黑龙江","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东","海南","四川","贵州","云南","陕西","甘肃","青海","台湾","广西","内蒙古","西藏","宁夏","新疆","香港","澳门"};

	/**
	 * 加载国别名称映射文件
	 */
	static {
		ClassLoader loader = AnalysisAction.class.getClassLoader();
		Properties ac = new Properties();
		try (InputStream in = loader.getResourceAsStream("ac.properties");
				InputStreamReader isr = new InputStreamReader(in, "UTF-8");) {
			ac.load(isr);
			AC_CODE_MAP = (Map) ac;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
