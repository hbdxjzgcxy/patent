package com.xdtech.patent.action;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.xdtech.search.client.LOGIC;
import com.xdtech.search.client.QueryObjectBuilder;
import com.xdtech.search.client.ws.BooleanQuery;
import com.xdtech.search.client.ws.SearchResult;

@Controller
@RequestMapping("/patent")
public class PatentAction extends SearchAction{

	/**
	 * 比对专利
	 * @param range
	 * @param fields
	 * @param filetype
	 * @return
	 */
	@RequestMapping("/compare")
	public ModelAndView compare(@RequestParam(value = "docNo") String docNo,@RequestParam(value = "word") String word) {
		ModelAndView mv = new ModelAndView("/patent_compare");
		String[] docNoArray = docNo.split(",");
		List<SearchBean> resultList = new ArrayList<SearchBean>();
		for(String no:docNoArray){
			SearchResult result = searchByDocNo(getDocNo(currentUser.getId(),no),word);
			resultList.addAll(toResultBeanList(result.getDocuments(), SearchBean.class, true));
		}
		
		Map<String,List<String>> map =new HashMap<String,List<String>>();
		Field[] fields = SearchBean.class.getDeclaredFields();
		for(Field f:fields){
			try {
				List<String> value = new LinkedList<String>();
				String fieldName = f.getName();
				for(SearchBean bean:resultList){
					String v = (String) MethodUtils.invokeMethod(bean,"get"+fieldName,null);
					value.add(v);
				}
				map.put(code2name(fieldName), value);
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
		
		return mv.addObject("map",map).addObject("word",word);
	}
	
	private SearchResult searchByDocNo(String docNo,String word){
		QueryObjectBuilder builder = QueryObjectBuilder.newInstance();
		BooleanQuery bq = newBooleanQ(newQueryClause(newTermQuery("docNo", docNo), LOGIC.AND));
		builder.setQuery(bq);
		builder.addCore(CORE_NAME);
		builder.addReturnField("TI", true, Integer.MAX_VALUE).addReturnField("AB", true, Integer.MAX_VALUE)
				.addReturnField("LS", false, Integer.MAX_VALUE).addReturnField("AN", false, Integer.MAX_VALUE)
				.addReturnField("PA", true, Integer.MAX_VALUE).addReturnField("AD", false, 100).addReturnField("PN", false, 100)
				.addReturnField("PD", false, 100).addReturnField("ADDR", false, 100).addReturnField("IPC", false, 100)
				.addReturnField("AGT", false, 100).addReturnField("AGC", false, 100);

//		builder.setHightlightWord(word);
		return search(builder);
	}
	
	private String code2name(String field) {
		String code = null;
		if(!StringUtils.isEmpty(field)){
			
			if (("TI".equals(field))) {
				code = "名称";
			} else if ("AB".equals(field)) {
				code = "摘要";
			} else if ("AC".equals(field)) {
				code = "专利申请国/地区/组织";
			} else if ("ADDR".equals(field)) {
				code = "申请（专利权）人地址";
			} else if ("AU".equals(field)) {
				code = "发明人";
			} else if ("PA".equals(field)) {
				code = "申请人";
			} else if ("AN".equals(field)) {
				code = "申请号";
			} else if ("AD".equals(field)) {
				code = "申请日";
			} else if ("PN".equals(field)) {
				code = "公开号";
			} else if ("PD".equals(field)) {
				code = "公开日";
			} else if ("PR".equals(field)) {
				code = "优先权";
			} else if ("CLM".equals(field)) {
				code = "权利要求";
			} else if ("IPC".equals(field)) {
				code = "IPC";
			} else if ("PFN".equals(field)) {
				code = "同族";
			}else if ("CDN".equals(field)) {
				code = "引证";
			}else if ("FT".equals(field)){
				code = "说明书";
			}
		}
		return code;
	}
	
}
