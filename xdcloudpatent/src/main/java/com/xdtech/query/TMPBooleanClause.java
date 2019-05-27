package com.xdtech.query;

/**
 * 带有符号的语句
 * 
 * @author zhangjianbing@msn.com
 *
 */
public class TMPBooleanClause {
	/** 逻辑运算符 */
	private String logic;
	/** query */
	private TMPQuery query;
	
	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public TMPQuery getQuery() {
		return query;
	}

	public void setQuery(TMPQuery query) {
		this.query = query;
	}
}
