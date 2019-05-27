package com.xdtech.patent.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class CNPatentProcessTemplate {
	
	

	public static String CHINA = "CN";

	/** 
	 * 申请种类
	 * @param PN 公开号
	 */
	public static String applTypeCN(String PN) {
		if (StringUtils.isEmpty(PN))
			return "";
		String applType = APT(PN);
		String applCN = "发明";
		if ("3".equals(applType))
			applCN = "外观设计";
		else if ("2".equals(applType))
			applCN = "实用新型";
		return applCN;
	}

	/**
	 * Application Type
	 * @param PN
	 * @return
	 */
	private static String APT(String PN) {
		String pn = PN.replace(" ", "");//CN88100001A/CN 88100001 A都是正确格式
		String applType = null;
		if (pn.length() == 11) {//1985-1988
			applType = StringUtils.substring(pn, 4, 5);
		} else {//历次编号体系的修改中，申请号变动较大，公开号只是位数加大结构未有较大变化。
			applType = StringUtils.substring(pn, 2, 3);
		}
		return applType;
	}

	/** 
	 * 法律事件
	 * @param str 法律状态描述性信息或其它
	 */
	public static String lawEvent(String str) {
		if (StringUtils.isEmpty(str))
			return "";
		return str;
	}
	
	/**
	 * 解析法律状态
	 * @param legal
	 * @return
	 */
	public static List<Map<String,String>> analyticalLegalStatus(String legal){
		String separator = "法律状态公告日";
		if(!StringUtils.isEmpty(legal)){
			if(StringUtils.indexOf(legal, separator) != -1){
				String[] items = legal.split(separator);
				List<Map<String,String>> result = new LinkedList<Map<String,String>>();
				for(String item : items){
					String[] str = StringUtils.split(item, ";");
					if(str != null && str.length > 0){
						Map<String,String> map = new LinkedHashMap<String,String>();
						for(String s:str){
							s = s.replace(" ", "").replace("：",":").trim();
							String key = StringUtils.substringBefore(s, ":");
							String val = StringUtils.substringAfter(s, ":");
							if(StringUtils.isEmpty(key)) key = separator;
							map.put(key, val);
						}
						result.add(map);
					}
				}
				return result;
			}
		}
		return null;
	}
	
	/**
	 * 法律状态
	 * @param status 法律状态
	 * @return
	 */
	public static String legalStatus(String status){
		if(StringUtils.isNotEmpty(status)){
			
			if(StringUtils.indexOf(status, "终止") != -1 ||
					StringUtils.indexOf(status, "放弃") != -1 ||
					StringUtils.indexOf(status, "有效期届满") != -1 ||
					StringUtils.indexOf(status, "未缴年费") != -1 ||
					StringUtils.indexOf(status, "其它有关事项") != -1||
					StringUtils.indexOf(status, "重复授权") != -1||
					StringUtils.indexOf(status, "无效") != -1 ||
					StringUtils.indexOf(status, "撤销") != -1 ||
					StringUtils.indexOf(status, "撤回") != -1 ||
					StringUtils.indexOf(status, "驳回") != -1){
				return "无权";
				
			}else if(StringUtils.indexOf(status, "恢复") != -1 ||
					StringUtils.indexOf(status, "部分无效") != -1 ||
					StringUtils.indexOf(status, "授权") != -1 ||
					StringUtils.indexOf(status, "授予") != -1 ||
					StringUtils.indexOf(status, "部分撤销") != -1||
					StringUtils.indexOf(status, "更正") != -1 ||
					StringUtils.indexOf(status, "有效") != -1 || 
					StringUtils.indexOf(status, "转移") != -1 || 
					StringUtils.indexOf(status, "变更") != -1 ){
				return "有权";
				
			}else if(StringUtils.indexOf(status, "公开") != -1 ||
					StringUtils.indexOf(status, "生效") != -1 ||
					StringUtils.indexOf(status, "实质审查") != -1 ||
					(StringUtils.indexOf(status, "申请") != -1 && StringUtils.indexOf(status, "恢复") != -1 )||
					StringUtils.indexOf(status, "恢复程序") != -1||
					StringUtils.indexOf(status, "恢复公告") != -1){
				return "审中";
			}
			
		}
		return "";
	}
	
	
	public static void main(String[] args) {
		String ls  ="法律状态公告日：20150722;法律状态：公开;描述信息：公开;法律状态公告日：20150819;法律状态：实质审查的生效;描述信息：实质审查的生效IPC(主分类):G01M   7/08;&nbsp;&nbsp;申请日:20150409;";
		List<Map<String,String>> list = analyticalLegalStatus(ls);
		for(int i = 0;i<list.size();i++){
			Map<String,String> map  = list.get(i);
			System.out.println("第"+i+"组数据");
			System.out.println(map);
//			for(String key:map.keySet()){
//				System.out.println(key+" === "+map.get(key));
//			}
		}
	}
}
