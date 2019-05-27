package com.xdtech.patent.action;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xdtech.patent.ipc.IPCContext;
import com.xdtech.patent.model.GraphModel;
import com.xdtech.patent.model.NamedData;
import com.xdtech.search.client.util.ClientUtil;
import com.xdtech.search.client.ws.FacetCount;
import com.xdtech.search.client.ws.FacetsResult;
import com.xdtech.search.client.ws.FieldFacetResult;
import com.xdtech.search.client.ws.PivotFacetResult;
import com.xdtech.search.client.ws.PivotResultSet;
import com.xdtech.search.client.ws.RangeFacetResult;
import com.xdtech.search.client.ws.SearchResult;

/**
 * 生成图表数据
 * 
 * @author sunjp
 *
 */
@Controller
public class GraphAction extends AnalysisAction {

	private static final long serialVersionUID = 2692807061174926183L;

	/**
	 * 生成图表数据
	 * 
	 * @param form
	 * @param response
	 */
	@RequestMapping("/graph")
	public void createGraph(AnalysisForm form, HttpServletResponse response) {
		GraphModel gm = buildModel(form);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		try {
			response.getWriter().print(toJsonStr(gm));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建图表模型数据
	 * 
	 * @param form
	 * @return
	 */
	private GraphModel buildModel(AnalysisForm form) {
		GraphModel gm = new GraphModel();
		ModelAndView mv = doAnalysis(form);
		SearchResult result = (SearchResult) mv.getModelMap().get("search_result");
		//ClientUtil.print(result);
		FacetsResult faceResult = result.getFacets();
		String xAxis = form.getXaxis();
		if (StringUtils.isNotEmpty(xAxis)) {
			// 柱状图 或折线图 或其他基于x-y坐标系类型的图
			if (ArrayUtils.isEmpty(form.getField())) {
				// 特殊处理 AD_Y_S PD_Y_S
				List data = Lists.newArrayList();
				List categories = Lists.newArrayList();
				if ("AD_Y_S".equals(xAxis) || "PD_Y_S".equals(xAxis)) {
					RangeFacetResult rfr = faceResult.getRangeFacets().get(0);
					for (FacetCount fc : rfr.getResult()) {
						categories.add(fc.getValue());
						data.add(fc.getCount());
					}
					// AC中替换为中文的国家名称
				} else if ("AC".equals(xAxis)) {
					FieldFacetResult rfr = faceResult.getFieldFacets().get(0);
					for (FacetCount fc : rfr.getResult()) {
						String value = AC_CODE_MAP.get(fc.getValue());
						categories.add(value ==null ?"未知" : value);
						data.add(fc.getCount());
					}
				} else {
					FieldFacetResult rfr = faceResult.getFieldFacets().get(0);
					for (FacetCount fc : rfr.getResult()) {
						categories.add(fc.getValue());
						data.add(fc.getCount());
					}
				}
				gm.setCategories(categories);
				gm.setData(data);
			} else {
				// (既有xaxis又有field字段)二维统计，需要pivot（需要多个统计结果）
				if (("PA_S".equals(xAxis) || "AU_S".equals(xAxis) || "AC".equals(xAxis) || "MIPC_B_S".equals(xAxis))
						&& "IPC_B_S".equals(form.getField()[0])) {
					// 申请人、发明人技术领域分析
					String[] arr = new String[] { "A", "B", "C", "D", "E", "F", "G", "H" };
					setBarGraphData(arr, gm, faceResult, xAxis);

				} else if (("PA_S".equals(xAxis) || "AU_S".equals(xAxis) || "AC".equals(xAxis)
						|| "IPC_B_S".equals(xAxis)) && "AD_Y_S".equals(form.getField()[0])) {
					// 申请人、发明人趋势分析

					String[] arr = getYear(10);
					setBrokenGraphData(arr, gm, faceResult, xAxis);

				} else {
					List data1 = Lists.newArrayList();
					List data2 = Lists.newArrayList();
					List data = Lists.newArrayList();
					List categories = Lists.newArrayList();
					PivotResultSet prs = faceResult.getPivotFacets().get(0);
					for (PivotFacetResult pfr : prs.getResultSet()) {
						categories.add(pfr.getValue());
						data1.add(pfr.getCount());
						data2.add(pfr.getPivots().size());
					}
					data.add(data1);
					data.add(data2);
					gm.setCategories(categories);
					gm.setData(data);
				}
			}
		} else {
			// 饼图 或其他非x-y坐标系图
			String[] field = form.getField();
			String pivot1 = field[0]; // 第一层
			if (field.length == 1) {
				// 柱状图或折线图
				if ("AD_Y_S".equals(pivot1) || "PD_Y_S".equals(pivot1)) {
					RangeFacetResult rfr = faceResult.getRangeFacets().get(0);
					List<Map<String, String>> list = Lists.newArrayList();
					rfr.getResult().forEach(e -> {
						Map<String, String> item = Maps.newHashMap();
						long count = e.getCount();
						String value = e.getValue();
						item.put("name", value);
						item.put("value", count + "");
						list.add(item);
					});
					gm.setData(list);
				} else {
					FieldFacetResult ffr = faceResult.getFieldFacets().get(0);
					if ("0".equals(form.getCtype())) {// 自定义图
						List<Map<String, String>> list = Lists.newArrayList();
						ffr.getResult().forEach(e -> {
							Map<String, String> item = Maps.newHashMap();
							long count = e.getCount();
							String value = e.getValue();
							if ("".equals(value)) {
								return;
							}
							String desc = "";
							if ("AC".equals(pivot1)) {
								item.put("code", value);
								value = AC_CODE_MAP.get(value); // 替换成中文的国家名称
							} else if ("MIPC_DL_S".equals(pivot1)) {
								desc = IPCContext.getDesc(value);
								item.put("desc", desc); // 附加一个 IPC_DL_S的一个描述
							}else if("PC".equals(pivot1)){
								if(!Arrays.asList(pc_code).contains(value)){
									return;
								}
							}
							item.put("name", value);
							item.put("value", count + "");
							list.add(item);
						});
						gm.setData(list);
					} else {
						List<List<Object>> list = Lists.newArrayList();
						for (FacetCount fc : ffr.getResult()) {
							 if("".equals(fc.getValue())){
							 continue;
							 }
							list.add(Lists.newArrayList(fc.getValue(), fc.getCount()));
						}
						gm.setData(list);
					}
				}
			} else {
				// 属性有多个的时候
				if (("AC".equals(pivot1) || "IPC_B_S".equals(pivot1) || "PA_S".equals(pivot1) || "AU_S".equals(pivot1))
						&& ("AC".equals(field[1]) || "PA_S".equals(field[1]) || "AU_S".equals(field[1]))) {
					setMultiFieldData(faceResult, field, gm);
				} else {
					List<Map<String, String>> list = Lists.newArrayList();
					PivotResultSet prs = faceResult.getPivotFacets().get(0);
					for (PivotFacetResult pfr : prs.getResultSet()) {
						Map<String, String> item = Maps.newHashMap();
						item.put("name", pfr.getValue());
						item.put("value", pfr.getCount() + "");
						item.put("count", pfr.getPivots().size() + "");
						list.add(item);
					}
					gm.setData(list);
				}
			}
		}
		return gm;
	}

}
