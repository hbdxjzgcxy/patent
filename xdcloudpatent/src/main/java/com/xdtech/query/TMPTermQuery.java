package com.xdtech.query;

/**
 * 用于中间转换的termquery
 * 
 * @author zhangjianbing@mns.com
 *
 */
public class TMPTermQuery extends TMPQuery {
	/** 字段名称，cntFunction=true的时候是包含字段名称的函数 */
	private String field;
	/** 值 */
	private String value;
	/** 是否完整匹配 */
	private boolean complete;
	/** 是否是数量函数 */
	private boolean cntFunction;
	/** 权重 */
	private float boost = 1.0f;
	/** 是否是短语搜索 */
	private boolean phrase;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public boolean isCntFunction() {
		return cntFunction;
	}

	public void setCntFunction(boolean cntFunction) {
		this.cntFunction = cntFunction;
	}

	public float getBoost() {
		return boost;
	}

	public void setBoost(float boost) {
		this.boost = boost;
	}

	public boolean isPhrase() {
		return phrase;
	}

	public void setPhrase(boolean phrase) {
		this.phrase = phrase;
	}
}
