package com.xdtech.parser;

import com.xdtech.search.client.ws.BooleanQuery;

public class ParseResult {
	String term;

	String queryStr;

	BooleanQuery query;

	public String getTerm() {
		return term.replace(" ", ",");
	}

	protected void setTerm(String term) {
		this.term = term;
	}

	public BooleanQuery getQuery() {
		return query;
	}

	protected void setQuery(BooleanQuery query) {
		this.query = query;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
}
