package com.xdtech.patent.action;

import org.joda.time.DateTime;

/**
 * 专利分析表单
 * 
 * @author changfei
 *
 */
public class AnalysisForm extends SearchForm {
	/** charts type 图类型 */
	private String ctype; // 0 自定义
	/**
	 * 坐标图显示时，x轴的定义 x轴为时间轴的字段 AD_Y_S ,PD_Y_S x轴其他字段：PA_S,AU_S等等
	 * 
	 */
	private String xasix;

	/**
	 * x轴开始坐标 x_start 取值范围规范说明 1）如果x_start>0,认为客户端需要设置开始年份
	 * 2）如果x_start<0,认为客户端需要用x_end+x_start作为开始年份
	 * 
	 */
	private int start;

	private int end = DateTime.now().getYear();

	/** 排序方式 */
	private String sort = "count"; // sort | index

	/** x轴显示坐标点个数 */
	private int dot = 10;
	
	/** 分析的字段 */
	private String[] field;

	/** 是否pivot分析 */
	private int pivot = 0;
	
	/**返回的数据量*/
	private int limit;
	
	/**是否统计专利数量*/
	private int isCount;

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public String getXaxis() {
		return xasix;
	}

	public void setXaxis(String xAxis) {
		this.xasix = xAxis;
	}

	public int getStart() {
		return start > 0 ? start : start + end;
	}

	public void setStart(int x_start) {
		this.start = x_start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int x_end) {
		this.end = x_end;
	}

	public int getGap() {
		return (getEnd() - getStart()) / getDot();
	}

	public int getDot() {
		return dot;
	}

	public void setDot(int x_dot) {
		this.dot = x_dot;
	}

	public String[] getField() {
		return field;
	}

	public void setField(String[] field) {
		this.field = field;
	}

	public int getPivot() {
		return pivot;
	}

	public void setPivot(int pivot) {
		this.pivot = pivot;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getIsCount() {
		return isCount;
	}

	public void setIsCount(int isCount) {
		this.isCount = isCount;
	}
	
	
	
}
