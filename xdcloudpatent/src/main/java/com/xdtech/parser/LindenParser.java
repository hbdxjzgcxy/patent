package com.xdtech.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermRangeQuery;

import com.xdtech.query.TMPBooleanClause;
import com.xdtech.query.TMPBooleanQuery;
import com.xdtech.query.TMPQuery;
import com.xdtech.query.TMPRangeQuery;
import com.xdtech.query.TMPTermQuery;

public class LindenParser {

	/**
	 * 进行转换，主要是将termQuery中的range部分分割出来。
	 * 
	 * @param defaultField
	 * @param queryString
	 * @return
	 * @throws Exception
	 */
	public static TMPQuery parse(String defaultField, String queryString) throws Exception {
		QueryParser parser = new QueryParser(defaultField);
		Query query = parser.parse(queryString);
		TMPQuery result = parse(query);
		return result;
	}

	/**
	 * 将lucene的query转换成
	 * 
	 * @param query
	 * @return
	 */
	private static TMPQuery parse(Query query) {
		TMPQuery tmpQuery = null;
		if (query instanceof BooleanQuery) {
			tmpQuery = changeBooleanQuery2TMPBooleanQuery((BooleanQuery) query);
		} else if (query instanceof XDTermQuery) {
			tmpQuery = changeTermQuery((XDTermQuery) query);
		} else if (query instanceof TermRangeQuery) {
			tmpQuery = changeTermRangeQuery2TMPRangeQuery((TermRangeQuery) query);
		} else if (query instanceof BoostQuery) {
			tmpQuery = changeBoostQuery2TMPQuery((BoostQuery) query);
		} else if (query instanceof PhraseQuery) {
			tmpQuery = changePhraseQuery2TMPTermQuery((PhraseQuery) query);
		}
		return tmpQuery;
	}

	private static TMPQuery changePhraseQuery2TMPTermQuery(PhraseQuery query) {
		Term[] terms = query.getTerms();
		String field = terms[0].field();
		String value = findValueFromPhraseQuery(query);
		TMPTermQuery result = new TMPTermQuery();
		result.setField(field);
		result.setPhrase(true);
		result.setValue(value);
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

	private static TMPBooleanQuery changeBooleanQuery2TMPBooleanQuery(BooleanQuery query) {
		List<BooleanClause> clauses = query.clauses();
		List<TMPBooleanClause> tmpClauses = new ArrayList<TMPBooleanClause>(clauses.size());
		for (BooleanClause clause : clauses) {
			Query cQuery = clause.getQuery();
			Occur occur = clause.getOccur();
			TMPQuery tmpQuery = parse(cQuery);
			TMPBooleanClause tmpClause = new TMPBooleanClause();
			tmpClause.setQuery(tmpQuery);
			if (occur.equals(Occur.MUST)) {
				tmpClause.setLogic("+");
			} else if (occur.equals(Occur.MUST_NOT)) {
				tmpClause.setLogic("-");
			} else {
				tmpClause.setLogic(" ");
			}
			tmpClauses.add(tmpClause);
		}
		TMPBooleanQuery result = new TMPBooleanQuery();
		result.setQuerys(tmpClauses);
		return result;
	}

	/**
	 * 带权重
	 * 
	 * @param query
	 * @return
	 */
	private static TMPQuery changeBoostQuery2TMPQuery(BoostQuery boostQuery) {
		Query query = boostQuery.getQuery();
		TMPQuery result = parse(query);
		return result;
	}

	/**
	 * 将范围搜索的query转换成TMPRangeQuery
	 * 
	 * @param query
	 * @return
	 */
	private static TMPQuery changeTermRangeQuery2TMPRangeQuery(TermRangeQuery query) {
		String field = query.getField();
		String lower = query.getLowerTerm().toString();
		String upper = query.getUpperTerm().toString();
		boolean includeLower = query.includesLower();
		boolean includeUpper = query.includesUpper();
		boolean cntFunction = isFunction(field);
		TMPRangeQuery tmpRangeQuery = new TMPRangeQuery();
		tmpRangeQuery.setCntFunction(cntFunction);
		tmpRangeQuery.setField(field);
		tmpRangeQuery.setIncludeLower(includeLower);
		tmpRangeQuery.setIncludeUpper(includeUpper);
		tmpRangeQuery.setLower(lower);
		tmpRangeQuery.setUpper(upper);
		return tmpRangeQuery;
	}

	/**
	 * 转换
	 * 
	 * @param query
	 * @return
	 */
	private static TMPQuery changeTermQuery(XDTermQuery query) {
		TMPQuery result = null;
		String op = query.getOp();
		if (op == null || "=".equals(op)) {
			result = changeTermQuery2TMPTermQuery(query);
		} else {
			result = changeTermQuery2TMPRangeQuery(query);
		}
		return result;
	}

	/**
	 * 将XDTermQuery转换成TMPRangeQuery
	 * 
	 * @param query
	 * @return
	 */
	private static TMPRangeQuery changeTermQuery2TMPRangeQuery(XDTermQuery query) {
		String op = query.getOp().trim();
		String lower = null;
		String upper = null;
		boolean includeLower = false;
		boolean includeUpper = false;
		Term term = query.getTerm();
		String field = term.field();
		boolean cntFunction = isFunction(field);// 是否是函数
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
		TMPRangeQuery tmpRangeQuery = new TMPRangeQuery();
		tmpRangeQuery.setCntFunction(cntFunction);
		tmpRangeQuery.setField(field);
		tmpRangeQuery.setIncludeLower(includeLower);
		tmpRangeQuery.setIncludeUpper(includeUpper);
		tmpRangeQuery.setLower(lower);
		tmpRangeQuery.setUpper(upper);
		return tmpRangeQuery;
	}

	/**
	 * 转换成TMPTermQuery
	 * 
	 * @param query
	 * @return
	 */
	private static TMPTermQuery changeTermQuery2TMPTermQuery(XDTermQuery query) {
		Term term = query.getTerm();
		String field = term.field();// 字段
		boolean cntFunction = isFunction(field);// 是否是函数
		float boost = 1.0f;// 权重
		String value = null;// 值
		TMPTermQuery tempQuery = new TMPTermQuery();
		value = term.text();
		boolean complete = isComplete(value);// 是否是完全匹配
		if (complete) {
			value = value.substring(1, value.length() - 1);
		}
		tempQuery.setField(field);
		tempQuery.setValue(value);
		tempQuery.setBoost(boost);
		tempQuery.setComplete(complete);
		tempQuery.setCntFunction(cntFunction);
		return tempQuery;
	}

	/**
	 * 判断是否是完整匹配
	 * 
	 * @param value
	 * @return
	 */
	private static boolean isComplete(String value) {
		boolean complete = false;
		if (value.startsWith("/") && value.endsWith("/")) {
			complete = true;
		}
		return complete;
	}

	/**
	 * 判断是否是函数
	 * 
	 * @param field
	 * @return
	 */
	private static boolean isFunction(String field) {
		boolean cntFunction = false;// 是否是函数
		if (field.toLowerCase().replace(" ", "").startsWith("cnt(")) {
			cntFunction = true;
		}
		return cntFunction;
	}

	public static void main(String[] args) {
		String queryString = "\"SANVSC (Beijing) Magnetics*\"";
		String defaultField = "TAB";
		try {
			TMPQuery query = parse(defaultField, queryString);
			System.out.println(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
