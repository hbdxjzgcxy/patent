package com.xdtech.parser;

import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;

import com.xdtech.search.client.LOGIC;
import com.xdtech.search.client.util.ClientUtil;
import com.xdtech.search.client.ws.QueryClause;

public class PatentParser {
	public static ParseResult parse(String qstr, String df) {
		Set<String> terms = new HashSet<String>();
		ParseResult result = new ParseResult();
		Query query = null;
		QueryParser parser = new QueryParser(df);
		try {
			query = parser.parse(qstr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (query != null) {
			result.setQuery(convert(query, terms));
		}
		StringBuilder sb = new StringBuilder();
		for (String t : terms) {
			sb.append(" ").append(t);
		}
		result.setTerm(terms.toString().trim());
		return result;
	}

	private static com.xdtech.search.client.ws.BooleanQuery convert(Query q, Set<String> termb) {
		com.xdtech.search.client.ws.BooleanQuery bq = null;
		
		 if (q instanceof XDTermQuery) {
			XDTermQuery xdq = (XDTermQuery) q;
			String op = xdq.getOp();
			if (op == null || op.equals("=")) {
				QueryClause qc = ClientUtil.newQueryClause(convert((XDTermQuery) q, termb), LOGIC.OR);
				return ClientUtil.newBooleanQ(qc);
			} else {
				QueryClause qc = ClientUtil.newQueryClause(convertX((XDTermQuery) q, termb), LOGIC.OR);
				return ClientUtil.newBooleanQ(qc);
			}
		}
		else if (q instanceof TermRangeQuery) {
			QueryClause qc = ClientUtil.newQueryClause(convert((TermRangeQuery) q), LOGIC.OR);
			return ClientUtil.newBooleanQ(qc);
		} else if (q instanceof PhraseQuery) {
			QueryClause qc = ClientUtil.newQueryClause(convert((PhraseQuery) q), LOGIC.OR);
			return ClientUtil.newBooleanQ(qc);
		} else if (q instanceof BoostQuery) {
			BoostQuery boostQ = (BoostQuery) q;
			com.xdtech.search.client.ws.BooleanQuery bq1 = convert(boostQ.getQuery(), termb);
			bq1.setBoost(boostQ.getBoost());
			return bq1;
		} else if (q instanceof BooleanQuery) {
			com.xdtech.search.client.ws.BooleanQuery rtbq = new com.xdtech.search.client.ws.BooleanQuery();
			BooleanQuery bq1 = (BooleanQuery) q;
			for (BooleanClause bc : bq1.clauses()) {
				bc.getOccur();
				com.xdtech.search.client.ws.BooleanQuery bqx = convert(bc.getQuery(), termb);
				rtbq.getClasuses().add(ClientUtil.newQueryClause(bqx, convert(bc.getOccur())));
			}
			return rtbq;
		} else if (q instanceof TermQuery) {
			QueryClause qc = ClientUtil.newQueryClause(convert((TermQuery) q, termb), LOGIC.OR);
			return ClientUtil.newBooleanQ(qc);
		} 
		return bq;
	}

	/**
	 * 将XDTermQuery转换成TMPRangeQuery
	 * 
	 * @param query
	 * @return
	 */
	private static com.xdtech.search.client.ws.RangeQuery convertX(XDTermQuery query, Set<String> termb) {
		String op = query.getOp().trim();
		String lower = null;
		String upper = null;
		boolean includeLower = false;
		boolean includeUpper = false;
		Term term = query.getTerm();
		String field = term.field();
		//boolean cntFunction = isFunction(field);// 是否是函数
		if (">".equals(op)) {
			// a>1 a:{1 TO *}
			lower = term.text();
			upper = "*";
		} else if ("<".equals(op)) {
			// a<1 a:{* TO 1}
			lower = "*";
			upper = term.text();
		} else if (">=".equals(op)) {
			// a>=1 a:[1 TO *}
			lower = term.text();
			upper = "*";
			includeLower = true;
		} else if ("<=".equals(op)) {
			// a<=1 a:{* TO 1]
			lower = "*";
			upper = term.text();
			includeUpper = true;
		}
		com.xdtech.search.client.ws.RangeQuery tmpRangeQuery = new com.xdtech.search.client.ws.RangeQuery();
		//tmpRangeQuery.setCntFunction(cntFunction);
		tmpRangeQuery.setField(field);
		//tmpRangeQuery.setIncludeLower(includeLower);
		//tmpRangeQuery.setIncludeUpper(includeUpper);
		if (includeLower || includeUpper) {
			tmpRangeQuery.setInclusive(true);
		}
		tmpRangeQuery.setStartValue(lower);
		tmpRangeQuery.setEndValue(upper);
		//termb.append(text)
		return tmpRangeQuery;
	}

	/**
	 * 转换成TMPTermQuery
	 * 
	 * @param query
	 * @return
	 */
	private static com.xdtech.search.client.ws.TermQuery convert(XDTermQuery query, Set<String> termb) {
		Term term = query.getTerm();
		String field = term.field();// 字段
		//boolean cntFunction = isFunction(field);// 是否是函数
		float boost = 1.0f;// 权重
		String value = null;// 值
		com.xdtech.search.client.ws.TermQuery termQ = new com.xdtech.search.client.ws.TermQuery();
		value = term.text();
		/*boolean complete = isComplete(value);// 是否是完全匹配
		if (complete) {
			value = value.substring(1, value.length() - 1);
		}*/
		termQ.setField(field);
		termQ.setValue(value);
		termQ.setBoost(boost);
		//tempQuery.setComplete(complete);
		//tempQuery.setCntFunction(cntFunction);
		termb.add(term.text());
		return termQ;
	}

	private static LOGIC convert(Occur oc) {
		if (oc == Occur.MUST) {
			return LOGIC.AND;
		}
		if (oc == Occur.MUST_NOT) {
			return LOGIC.NOT;
		}
		return LOGIC.OR;

	}

	private static com.xdtech.search.client.ws.RangeQuery convert(TermRangeQuery trq) {
		return ClientUtil.newRangQuery(trq.getField(), trq.getLowerTerm().toString(), trq.getUpperTerm().toString(), trq.includesLower());
	}

	private static com.xdtech.search.client.ws.TermQuery convert(TermQuery tq, Set<String> termb) {
		String field = tq.getTerm().field();
		String value = tq.getTerm().text();
		termb.add(value);
		return ClientUtil.newTermQuery(field, value);
	}

	private static com.xdtech.search.client.ws.TermQuery convert(PhraseQuery query) {
		Term[] terms = query.getTerms();
		String field = terms[0].field();
		String value = findValueFromPhraseQuery(query);
		com.xdtech.search.client.ws.TermQuery result = new com.xdtech.search.client.ws.TermQuery();
		result.setField(field);
		result.setValue("\"" + value + "\"");
		return result;
	}

	/**
	 * 从短语搜索中找到值
	 * @param query
	 * @return
	 */
	private static String findValueFromPhraseQuery(PhraseQuery query) {
		int[] positions = query.getPositions();
		StringBuilder buffer = new StringBuilder();
		int maxPosition;
		if (positions.length == 0)
			maxPosition = -1;
		else {
			maxPosition = positions[(positions.length - 1)];
		}

		String[] pieces = new String[maxPosition + 1];
		Term[] terms = query.getTerms();
		for (int i = 0; i < terms.length; i++) {
			int pos = positions[i];
			String s = pieces[pos];
			if (s == null)
				s = terms[i].text();
			else {
				s = new StringBuilder().append(s).append("|").append(terms[i].text()).toString();
			}
			pieces[pos] = s;
		}
		for (int i = 0; i < pieces.length; i++) {
			if (i > 0) {
				buffer.append(' ');
			}
			String s = pieces[i];
			if (s == null)
				buffer.append('?');
			else {
				buffer.append(s);
			}
		}

		int slop = query.getSlop();
		if (slop != 0) {
			buffer.append("~");
			buffer.append(slop);
		}
		return buffer.toString();
	}

	public static void main(String[] args) {
		parse("电视~10", "TI");
	}
}
