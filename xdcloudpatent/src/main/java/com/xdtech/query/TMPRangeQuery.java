package com.xdtech.query;

/**
 * 范围query
 * 
 * @author zhangjianbing@msn.com
 *
 */
public class TMPRangeQuery extends TMPQuery {
	/** 字段名称，cntFunction=true的时候是包含字段名称的函数 */
	private String field;
	/** 是否是count函数 */
	private boolean cntFunction;
	/** 下界 */
	private String lower;
	/** 上界 */
	private String upper;
	/** 是否包含下界 */
	private boolean includeLower;
	/** 是否包含上界 */
	private boolean includeUpper;
	/** 权重 */
	private float boost = 1.0f;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public boolean isCntFunction() {
		return cntFunction;
	}

	public void setCntFunction(boolean cntFunction) {
		this.cntFunction = cntFunction;
	}

	public String getLower() {
		return lower;
	}

	public void setLower(String lower) {
		this.lower = lower;
	}

	public String getUpper() {
		return upper;
	}

	public void setUpper(String upper) {
		this.upper = upper;
	}

	public boolean isIncludeLower() {
		return includeLower;
	}

	public void setIncludeLower(boolean includeLower) {
		this.includeLower = includeLower;
	}

	public boolean isIncludeUpper() {
		return includeUpper;
	}

	public void setIncludeUpper(boolean includeUpper) {
		this.includeUpper = includeUpper;
	}

	public float getBoost() {
		return boost;
	}

	public void setBoost(float boost) {
		this.boost = boost;
	}
}
