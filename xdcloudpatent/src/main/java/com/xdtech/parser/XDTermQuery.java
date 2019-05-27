package com.xdtech.parser;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;

public class XDTermQuery extends TermQuery {
	private String op;
	private boolean function;
	private Term term;

	public XDTermQuery(Term t) {
		super(t);
		this.term = t;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public boolean isFunction() {
		return function;
	}

	public void setFunction(boolean function) {
		this.function = function;
	}

	public String toString(String field) {
		StringBuilder buffer = new StringBuilder();
		if (!term.field().equals(field)) {
			buffer.append(term.field());
			buffer.append(op);
		}
		buffer.append(term.text());
		return buffer.toString();
	}
}
