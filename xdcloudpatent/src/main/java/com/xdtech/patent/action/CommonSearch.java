package com.xdtech.patent.action;

import org.apache.commons.lang.StringUtils;

/**
 * 检索条件辅助类。<p>
 * 普通检索：key=val
 * @author changfei
 *
 */
public class CommonSearch {
	private String field;
	private String value;

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

	@Override
	public String toString() {
		if (StringUtils.isEmpty(field) || StringUtils.isEmpty(value))
			return null;
		return field + "=" + value;
	}

	public String getQuery() {
		return toString();
	}

}
