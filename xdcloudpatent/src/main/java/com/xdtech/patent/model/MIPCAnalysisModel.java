package com.xdtech.patent.model;

public class MIPCAnalysisModel {
	private String desc;	//主分类号描述
	private String value;	//主分类号编码
	private Long count;		//主分类号统计数量
	private String percent;	//主分类号相对百分比
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
