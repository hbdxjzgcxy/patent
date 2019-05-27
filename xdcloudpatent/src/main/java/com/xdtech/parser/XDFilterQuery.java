package com.xdtech.parser;

import org.apache.lucene.search.Query;

/**
 * 用于处理filter函数的Query。这个放在在lindenpat平台上没有使用，在solr6.0中带上了这个Query，就加上了。
 * 
 * @author zhangjianbing@msn.com
 *
 */
public class XDFilterQuery extends Query {

	protected final Query q;

	public XDFilterQuery(Query q) {
		this.q = q;
	}

	public Query getQuery() {
		return q;
	}

	public String toString(String field) {
		StringBuilder sb = new StringBuilder();
		sb.append("filter(");
		sb.append(q.toString(""));
		sb.append(')');
		return sb.toString();
	}
}
