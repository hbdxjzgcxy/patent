package com.xdtech.parser;

import java.io.StringReader;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.QueryBuilder;

/**
 * 修改的Solr6.0中的org.apache.solr.parser.SolrQueryParserBase。
 * 
 * @author zhangjianbing@msn.com
 *
 */
public abstract class XDBaseParser extends QueryBuilder {

	static final int CONJ_NONE = 0;
	static final int CONJ_AND = 1;
	static final int CONJ_OR = 2;

	static final int MOD_NONE = 0;
	static final int MOD_NOT = 10;
	static final int MOD_REQ = 11;

	// make it possible to call setDefaultOperator() without accessing
	// the nested class:
	/** Alternative form of QueryParser.Operator.AND */
	public static final Operator AND_OPERATOR = Operator.AND;
	/** Alternative form of QueryParser.Operator.OR */
	public static final Operator OR_OPERATOR = Operator.OR;

	/** The default operator that parser uses to combine query terms */
	Operator operator = OR_OPERATOR;

	MultiTermQuery.RewriteMethod multiTermRewriteMethod = MultiTermQuery.CONSTANT_SCORE_REWRITE;
	boolean allowLeadingWildcard = true;

	String defaultField;
	int phraseSlop = 0; // default slop for phrase queries
	float fuzzyMinSim = FuzzyQuery.defaultMinSimilarity;
	int fuzzyPrefixLength = FuzzyQuery.defaultPrefixLength;

	boolean autoGeneratePhraseQueries = false;

	/**
	 * The default operator for parsing queries.
	 */
	public static enum Operator {
		OR, AND
	}

	/**
	 * Identifies the list of all known "magic fields" that trigger special
	 * parsing behavior
	 */
	public static enum MagicFieldName {
		VAL("_val_", "func"), QUERY("_query_", null);

		public final String field;
		public final String subParser;

		MagicFieldName(final String field, final String subParser) {
			this.field = field;
			this.subParser = subParser;
		}

		@Override
		public String toString() {
			return field;
		}

		private final static Map<String, MagicFieldName> lookup = new HashMap<String, MagicFieldName>();
		static {
			for (MagicFieldName s : EnumSet.allOf(MagicFieldName.class))
				lookup.put(s.toString(), s);
		}

		public static MagicFieldName get(final String field) {
			return lookup.get(field);
		}
	}

	// So the generated QueryParser(CharStream) won't error out
	public XDBaseParser() {
		super(null);
	}

	// the generated parser will create these in QueryParser
	public abstract void ReInit(CharStream stream);

	public abstract Query TopLevelQuery(String field) throws Exception;

	public void init(String defaultField) {
		this.defaultField = defaultField;
	}

	/**
	 * Parses a query string, returning a {@link org.apache.lucene.search.Query}
	 * .
	 * 
	 * @param query
	 *            the query string to be parsed.
	 */
	public Query parse(String query) throws Exception {
		ReInit(new FastCharStream(new StringReader(query)));
		try {
			// TopLevelQuery is a Query followed by the end-of-input (EOF)
			Query res = TopLevelQuery(null); // pass null so we can tell later
												// if an explicit field was
												// provided or not
			return res != null ? res : newBooleanQuery().build();
		} catch (ParseException | TokenMgrError tme) {
			throw new Exception("Cannot parse '" + query + "': "
					+ tme.getMessage(), tme);
		} catch (BooleanQuery.TooManyClauses tmc) {
			throw new Exception("Cannot parse '" + query
					+ "': too many boolean clauses", tmc);
		}
	}

	/**
	 * @return Returns the default field.
	 */
	public String getDefaultField() {
		return this.defaultField;
	}

	protected String explicitField;

	/** Handles the default field if null is passed */
	public String getField(String fieldName) {
		explicitField = fieldName;
		return fieldName != null ? fieldName : this.defaultField;
	}

	/**
	 * For a fielded query, returns the actual field specified (i.e. null if
	 * default is being used) myfield:A or myfield:(A B C) will both return
	 * "myfield"
	 */
	public String getExplicitField() {
		return explicitField;
	}

	/**
	 * @see #setAutoGeneratePhraseQueries(boolean)
	 */
	public final boolean getAutoGeneratePhraseQueries() {
		return autoGeneratePhraseQueries;
	}

	/**
	 * Set to true if phrase queries will be automatically generated when the
	 * analyzer returns more than one term from whitespace delimited text. NOTE:
	 * this behavior may not be suitable for all languages.
	 * <p>
	 * Set to false if phrase queries should only be generated when surrounded
	 * by double quotes.
	 */
	public final void setAutoGeneratePhraseQueries(boolean value) {
		this.autoGeneratePhraseQueries = value;
	}

	/**
	 * Get the minimal similarity for fuzzy queries.
	 */
	public float getFuzzyMinSim() {
		return fuzzyMinSim;
	}

	/**
	 * Set the minimum similarity for fuzzy queries. Default is 2f.
	 */
	public void setFuzzyMinSim(float fuzzyMinSim) {
		this.fuzzyMinSim = fuzzyMinSim;
	}

	/**
	 * Get the prefix length for fuzzy queries.
	 * 
	 * @return Returns the fuzzyPrefixLength.
	 */
	public int getFuzzyPrefixLength() {
		return fuzzyPrefixLength;
	}

	/**
	 * Set the prefix length for fuzzy queries. Default is 0.
	 * 
	 * @param fuzzyPrefixLength
	 *            The fuzzyPrefixLength to set.
	 */
	public void setFuzzyPrefixLength(int fuzzyPrefixLength) {
		this.fuzzyPrefixLength = fuzzyPrefixLength;
	}

	/**
	 * Sets the default slop for phrases. If zero, then exact phrase matches are
	 * required. Default value is zero.
	 */
	public void setPhraseSlop(int phraseSlop) {
		this.phraseSlop = phraseSlop;
	}

	/**
	 * Gets the default slop for phrases.
	 */
	public int getPhraseSlop() {
		return phraseSlop;
	}

	/**
	 * Set to <code>true</code> to allow leading wildcard characters.
	 * <p>
	 * When set, <code>*</code> or <code>?</code> are allowed as the first
	 * character of a PrefixQuery and WildcardQuery. Note that this can produce
	 * very slow queries on big indexes.
	 * <p>
	 * Default: false.
	 */
	public void setAllowLeadingWildcard(boolean allowLeadingWildcard) {
		this.allowLeadingWildcard = allowLeadingWildcard;
	}

	/**
	 * @see #setAllowLeadingWildcard(boolean)
	 */
	public boolean getAllowLeadingWildcard() {
		return allowLeadingWildcard;
	}

	/**
	 * Sets the boolean operator of the QueryParser. In default mode (
	 * <code>OR_OPERATOR</code>) terms without any modifiers are considered
	 * optional: for example <code>capital of Hungary</code> is equal to
	 * <code>capital OR of OR Hungary</code>.<br>
	 * In <code>AND_OPERATOR</code> mode terms are considered to be in
	 * conjunction: the above mentioned query is parsed as
	 * <code>capital AND of AND Hungary</code>
	 */
	public void setDefaultOperator(Operator op) {
		this.operator = op;
	}

	/**
	 * Gets implicit operator setting, which will be either AND_OPERATOR or
	 * OR_OPERATOR.
	 */
	public Operator getDefaultOperator() {
		return operator;
	}

	/**
	 * By default QueryParser uses
	 * {@link org.apache.lucene.search.MultiTermQuery#CONSTANT_SCORE_REWRITE}
	 * when creating a PrefixQuery, WildcardQuery or RangeQuery. This
	 * implementation is generally preferable because it a) Runs faster b) Does
	 * not have the scarcity of terms unduly influence score c) avoids any
	 * "TooManyBooleanClauses" exception. However, if your application really
	 * needs to use the old-fashioned BooleanQuery expansion rewriting and the
	 * above points are not relevant then use this to change the rewrite method.
	 */
	public void setMultiTermRewriteMethod(MultiTermQuery.RewriteMethod method) {
		multiTermRewriteMethod = method;
	}

	/**
	 * @see #setMultiTermRewriteMethod
	 */
	public MultiTermQuery.RewriteMethod getMultiTermRewriteMethod() {
		return multiTermRewriteMethod;
	}

	protected void addClause(List<BooleanClause> clauses, int conj, int mods,
			Query q) {
		boolean required, prohibited;

		// If this term is introduced by AND, make the preceding term required,
		// unless it's already prohibited
		if (clauses.size() > 0 && conj == CONJ_AND) {
			BooleanClause c = clauses.get(clauses.size() - 1);
			if (!c.isProhibited())
				clauses.set(clauses.size() - 1, new BooleanClause(c.getQuery(),
						BooleanClause.Occur.MUST));
		}

		if (clauses.size() > 0 && operator == AND_OPERATOR && conj == CONJ_OR) {
			// If this term is introduced by OR, make the preceding term
			// optional,
			// unless it's prohibited (that means we leave -a OR b but +a OR
			// b-->a OR b)
			// notice if the input is a OR b, first term is parsed as required;
			// without
			// this modification a OR b would parsed as +a OR b
			BooleanClause c = clauses.get(clauses.size() - 1);
			if (!c.isProhibited())
				clauses.set(clauses.size() - 1, new BooleanClause(c.getQuery(),
						BooleanClause.Occur.SHOULD));
		}

		// We might have been passed a null query; the term might have been
		// filtered away by the analyzer.
		if (q == null)
			return;

		if (operator == OR_OPERATOR) {
			// We set REQUIRED if we're introduced by AND or +; PROHIBITED if
			// introduced by NOT or -; make sure not to set both.
			prohibited = (mods == MOD_NOT);
			required = (mods == MOD_REQ);
			if (conj == CONJ_AND && !prohibited) {
				required = true;
			}
		} else {
			// We set PROHIBITED if we're introduced by NOT or -; We set
			// REQUIRED
			// if not PROHIBITED and not introduced by OR
			prohibited = (mods == MOD_NOT);
			required = (!prohibited && conj != CONJ_OR);
		}
		if (required && !prohibited)
			clauses.add(newBooleanClause(q, BooleanClause.Occur.MUST));
		else if (!required && !prohibited)
			clauses.add(newBooleanClause(q, BooleanClause.Occur.SHOULD));
		else if (!required && prohibited)
			clauses.add(newBooleanClause(q, BooleanClause.Occur.MUST_NOT));
		else
			throw new RuntimeException(
					"Clause cannot be both required and prohibited");
	}

	protected Query newFieldQuery(Analyzer analyzer, String field,
			String queryText, boolean quoted) throws Exception {
		BooleanClause.Occur occur = operator == Operator.AND ? BooleanClause.Occur.MUST
				: BooleanClause.Occur.SHOULD;
		return createFieldQuery(analyzer, occur, field, queryText, quoted
				|| autoGeneratePhraseQueries, phraseSlop);
	}

	/**
	 * Base implementation delegates to
	 * {@link #getFieldQuery(String,String,boolean)}. This method may be
	 * overridden, for example, to return a SpanNearQuery instead of a
	 * PhraseQuery.
	 * 
	 * @throws Exception
	 *
	 */
	protected Query getFieldQuery(String field, String queryText, int slop)
			throws Exception {
		Query query = getFieldQuery(field, queryText, true);
		return query;
	}

	/**
	 * Builds a new BooleanClause instance
	 * 
	 * @param q
	 *            sub query
	 * @param occur
	 *            how this clause should occur when matching documents
	 * @return new BooleanClause instance
	 */
	protected BooleanClause newBooleanClause(Query q, BooleanClause.Occur occur) {
		return new BooleanClause(q, occur);
	}

	/**
	 * 因为修改了jj文件，所有的query都是以termquery的方式返回结果，所以该方法应该不会被调用
	 * 
	 * @param prefix
	 *            Prefix term
	 * @return new PrefixQuery instance
	 */
	protected Query newPrefixQuery(Term prefix) {
		return new PrefixQuery(prefix);
	}

	/**
	 * 因为修改了jj文件，所有的query都是以termquery的方式返回结果，所以该方法应该不会被调用
	 * 
	 * @param prefix
	 *            regexp term
	 * @return new PrefixQuery instance
	 */
	protected Query newRegexpQuery(Term regexp) {
		RegexpQuery query = new RegexpQuery(regexp);
		return query;
	}

	/**
	 * Builds a new FuzzyQuery instance
	 * 
	 * @param term
	 *            Term
	 * @param minimumSimilarity
	 *            minimum similarity
	 * @param prefixLength
	 *            prefix length
	 * @return new FuzzyQuery Instance
	 */
	protected Query newFuzzyQuery(Term term, float minimumSimilarity,
			int prefixLength) {
		// FuzzyQuery doesn't yet allow constant score rewrite
		String text = term.text();
		int numEdits = FuzzyQuery.floatToEdits(minimumSimilarity,
				text.codePointCount(0, text.length()));
		return new FuzzyQuery(term, numEdits, prefixLength);
	}

	/**
	 * Builds a new MatchAllDocsQuery instance
	 * 
	 * @return new MatchAllDocsQuery instance
	 */
	protected Query newMatchAllDocsQuery() {
		return new MatchAllDocsQuery();
	}

	/**
	 * 因为修改了jj文件，所有的query都是以termquery的方式返回结果，所以该方法应该不会被调用
	 * 
	 * @param prefix
	 *            Prefix term
	 * @return new PrefixQuery instance
	 */
	protected Query newWildcardQuery(Term t) {
		WildcardQuery query = new WildcardQuery(t);
		return query;
	}

	/**
	 * Factory method for generating query, given a set of clauses. By default
	 * creates a boolean query composed of clauses passed in.
	 *
	 * Can be overridden by extending classes, to modify query being returned.
	 *
	 * @param clauses
	 *            List that contains
	 *            {@link org.apache.lucene.search.BooleanClause} instances to
	 *            join.
	 *
	 * @return Resulting {@link org.apache.lucene.search.Query} object.
	 */
	protected Query getBooleanQuery(List<BooleanClause> clauses)
			throws Exception {
		if (clauses.size() == 0) {
			return null; // all clause words were filtered away by the analyzer.
		}
		BooleanQuery.Builder query = newBooleanQuery();
		for (final BooleanClause clause : clauses) {
			query.add(clause);
		}
		return query.build();
	}

	// called from parser
	Query handleBareTokenQuery(String qfield, Token term, Token fuzzySlop,
			boolean prefix, boolean wildcard, boolean fuzzy, boolean regexp)
			throws Exception {
		Query q;
		String termImage = discardEscapeChar(term.image);
		q = getFieldQuery(qfield, termImage, false);
		return q;
	}

	// called from parser
	Query handleQuotedTerm(String qfield, Token term, Token fuzzySlop)
			throws Exception {
		int s = phraseSlop; // default
		if (fuzzySlop != null) {
			try {
				s = Float.valueOf(fuzzySlop.image.substring(1)).intValue();
			} catch (Exception ignored) {
			}
		}
		return getFieldQuery(qfield, discardEscapeChar(term.image.substring(1,
				term.image.length() - 1)), s);
	}

	// called from parser
	Query handleBoost(Query q, Token boost) {
		// q==null check is to avoid boosting null queries, such as those caused
		// by stop words
		if (boost == null || boost.image.length() == 0 || q == null) {
			return q;
		}
		if (boost.image.charAt(0) == '=') {
			// syntax looks like foo:x^=3
			float val = Float.parseFloat(boost.image.substring(1));
			Query newQ = q;
			newQ = new ConstantScoreQuery(q);
			return new BoostQuery(newQ, val);
		}

		float boostVal = Float.parseFloat(boost.image);

		return new BoostQuery(q, boostVal);
	}

	/**
	 * Returns a String where the escape char has been removed, or kept only
	 * once if there was a double escape.
	 *
	 * Supports escaped unicode characters, e. g. translates
	 * <code>\\u0041</code> to <code>A</code>.
	 *
	 */
	String discardEscapeChar(String input) throws Exception {
		// Create char array to hold unescaped char sequence
		char[] output = new char[input.length()];

		// The length of the output can be less than the input
		// due to discarded escape chars. This variable holds
		// the actual length of the output
		int length = 0;

		// We remember whether the last processed character was
		// an escape character
		boolean lastCharWasEscapeChar = false;

		// The multiplier the current unicode digit must be multiplied with.
		// E. g. the first digit must be multiplied with 16^3, the second with
		// 16^2...
		int codePointMultiplier = 0;

		// Used to calculate the codepoint of the escaped unicode character
		int codePoint = 0;

		for (int i = 0; i < input.length(); i++) {
			char curChar = input.charAt(i);
			if (codePointMultiplier > 0) {
				codePoint += hexToInt(curChar) * codePointMultiplier;
				codePointMultiplier >>>= 4;
				if (codePointMultiplier == 0) {
					output[length++] = (char) codePoint;
					codePoint = 0;
				}
			} else if (lastCharWasEscapeChar) {
				if (curChar == 'u') {
					// found an escaped unicode character
					codePointMultiplier = 16 * 16 * 16;
				} else {
					// this character was escaped
					output[length] = curChar;
					length++;
				}
				lastCharWasEscapeChar = false;
			} else {
				if (curChar == '\\') {
					lastCharWasEscapeChar = true;
				} else {
					output[length] = curChar;
					length++;
				}
			}
		}

		if (codePointMultiplier > 0) {
			throw new Exception("Truncated unicode escape sequence.");
		}

		if (lastCharWasEscapeChar) {
			throw new Exception("Term can not end with escape character.");
		}

		return new String(output, 0, length);
	}

	/** Returns the numeric value of the hexadecimal character */
	static final int hexToInt(char c) throws Exception {
		if ('0' <= c && c <= '9') {
			return c - '0';
		} else if ('a' <= c && c <= 'f') {
			return c - 'a' + 10;
		} else if ('A' <= c && c <= 'F') {
			return c - 'A' + 10;
		} else {
			throw new Exception(
					"Non-hex character in Unicode escape sequence: " + c);
		}
	}

	/**
	 * 特殊字符转义
	 * 
	 * @param s
	 * @return
	 */
	public static String escape(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			// These characters are part of the query syntax and must be escaped
			if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '('
					|| c == ')' || c == ':' || c == '^' || c == '[' || c == ']'
					|| c == '\"' || c == '{' || c == '}' || c == '~'
					|| c == '*' || c == '?' || c == '|' || c == '&' || c == '/') {
				sb.append('\\');
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 检查字段是否为null
	 * 
	 * @param field
	 * @throws Exception
	 */
	private void checkNullField(String field) throws Exception {
		if (field == null) {
			throw new NullPointerException("检索字段是空");
		}
	}

	/**
	 * 生成一个Query
	 * 
	 * @param field
	 * @param queryText
	 * @param quoted
	 * @return
	 * @throws Exception
	 */
	protected Query getFieldQuery(String field, String queryText, boolean quoted)
			throws Exception {
		checkNullField(field);
		return newFieldQuery(getAnalyzer(), field, queryText, quoted);
	}

	/**
	 * 默认为空格分隔的解析器
	 */
	public Analyzer getAnalyzer() {
		return new WhitespaceAnalyzer();
	}

	/**
	 * 获取range
	 * 
	 * @param field
	 * @param part1
	 * @param part2
	 * @param startInclusive
	 * @param endInclusive
	 * @return
	 * @throws Exception
	 */
	protected Query getRangeQuery(String field, String part1, String part2,
			boolean startInclusive, boolean endInclusive) throws Exception {
		checkNullField(field);
		TermRangeQuery query = new TermRangeQuery(field, new BytesRef(part1),
				new BytesRef(part2), startInclusive, endInclusive);
		return query;
	}

	// called from parser
	protected Query getLocalParams(String qfield, String lparams)
			throws Exception {
		// QParser nested = parser.subQuery(lparams, null);
		// return nested.getQuery();
		return null;
	}

	/**
	 * 处理filter函数
	 * 
	 * @param q
	 * @return
	 */
	Query getFilter(Query q) {
		return new XDFilterQuery(q);
	}

	/**
	 * 转换成XDTermQuery
	 * 
	 * @param query
	 * @param op
	 * @param function
	 * @return
	 */
	public Query XDTermQueryChange(Query query, String op, boolean function) {
		Query result = query;
		if (query instanceof TermQuery) {
			TermQuery termQuery = (TermQuery) query;
			Term t = termQuery.getTerm();
			XDTermQuery xdTermQuery = new XDTermQuery(t);
			xdTermQuery.setOp(op);
			xdTermQuery.setFunction(function);
			result = xdTermQuery;
		} else if (query instanceof BoostQuery) {
			BoostQuery boostQuery = (BoostQuery) query;
			Query sunQuery = boostQuery.getQuery();
			if (sunQuery instanceof TermQuery) {
				TermQuery termQuery = (TermQuery) sunQuery;
				Term t = termQuery.getTerm();
				XDTermQuery xdTermQuery = new XDTermQuery(t);
				xdTermQuery.setOp(op);
				xdTermQuery.setFunction(function);
				result = new BoostQuery(xdTermQuery, boostQuery.getBoost());
			}
		}
		return result;
	}
}
