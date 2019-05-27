package com.xdtech.patent.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.xdtech.search.client.ws.TermFacet;

/**
 * 检索表单
 * @author changfei
 *
 */
public class SearchForm {
	private String kwd = "";
	private String pkwd = null; //记录二次检索中上次检索条件
	private String searchType = "smart";
	private String core = null;
	private String path;
	private String tName;
	private List<String> AC;
	private List<String> db;

	///////////==== 统计字段====////////
	private List<String> LS;
	private List<String> CNLX;
	private List<String> PA_S;
	private List<String> AU_S;
	private List<String> AD_Y_S;
	private List<String> PD_Y_S;
	private List<String> MIPC_DL_S;
	private List<String> MIPC_XL_S;
	private Map<String, List<String>> filter;
	private List<TermFacet> termFacets;
	private String style = "list";
	private int pageNo = 1;
	private int pageSize = 10;
	private int facet;
	private String facetTarget;

	//table_search
	private List<String> fields;
	private String moreKwd;
	private String lessKwd;
	private String notKwd;

	//复杂检索条件
	private List<CommonSearch> s;
	private List<RangeSearch> rs;

	private String hlKwd;
	private String qstr;

	//二次检索标记
	private int ss;

	private List<String> sorts;

	public String getPkwd() {
		return pkwd;
	}

	public void setPkwd(String pkwd) {
		this.pkwd = pkwd;
	}

	public void setS(List<CommonSearch> s) {
		this.s = s;
	}

	public String getKwd() {
		if(kwd==null)
			kwd="";
		return kwd;
	}

	public void setKwd(String kwd) {
		this.kwd = kwd;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getCore() {
		return core;
	}

	public void setCore(String core) {
		if (StringUtils.isNotEmpty(core))
			this.core = core;
	}
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<String> getAC() {
		return AC;
	}

	public void setAC(List<String> AC) {
		this.AC = AC;
	}

	public List<String> getLS() {
		return LS;
	}

	public void setLS(List<String> lS) {
		LS = lS;
	}

	public List<String> getCNLX() {
		return CNLX;
	}

	public void setCNLX(List<String> cNLX) {
		CNLX = cNLX;
	}

	public int getFacet() {
		return facet;
	}

	public boolean ifFacet() {
		return facet == 1;
	}

	public String getFacetTarget() {
		if (facetTarget == null) {
			return "";
		}
		return facetTarget;
	}

	public void setFacetTarget(String facetTarget) {
		this.facetTarget = facetTarget;
	}

	public List<String> getPA_S() {
		return PA_S;
	}

	public void setPA_S(List<String> pA_S) {
		PA_S = pA_S;
	}

	public List<String> getAU_S() {
		return AU_S;
	}

	public void setAU_S(List<String> aU_S) {
		AU_S = aU_S;
	}

	public List<String> getAD_Y_S() {
		return AD_Y_S;
	}

	public void setAD_Y_S(List<String> aD_Y_S) {
		AD_Y_S = aD_Y_S;
	}

	public List<String> getPD_Y_S() {
		return PD_Y_S;
	}

	public void setPD_Y_S(List<String> pD_Y_S) {
		PD_Y_S = pD_Y_S;
	}

	public List<String> getMIPC_DL_S() {
		return MIPC_DL_S;
	}

	public void setMIPC_DL_S(List<String> mIPC_DL_S) {
		MIPC_DL_S = mIPC_DL_S;
	}

	public List<String> getMIPC_XL_S() {
		return MIPC_XL_S;
	}

	public void setMIPC_XL_S(List<String> mIPC_XL_S) {
		MIPC_XL_S = mIPC_XL_S;
	}

	public void setFacet(int isFacet) {
		this.facet = isFacet;
	}

	public List<TermFacet> getTermFacets() {
		return termFacets;
	}

	public void setTermFacets(List<TermFacet> termFacets) {
		this.termFacets = termFacets;
	}

	public Map<String, List<String>> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, List<String>> filter) {
		this.filter = filter;
	}

	public String getMoreKwd() {
		return moreKwd;
	}

	public void setMoreKwd(String moreKwd) {
		this.moreKwd = moreKwd;
	}

	public String getLessKwd() {
		return lessKwd;
	}

	public void setLessKwd(String lessKwd) {
		this.lessKwd = lessKwd;
	}

	public String getNotKwd() {
		return notKwd;
	}

	public void setNotKwd(String notKwd) {
		this.notKwd = notKwd;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public String getHlKwd() {
		return hlKwd;
	}

	public void setHlKwd(String hlKwd) {
		this.hlKwd = hlKwd;
	}

	public String getQstr() {
		return qstr;
	}

	public void setQstr(String qstr) {
		this.qstr = qstr;
	}

	public List<RangeSearch> getRs() {
		return rs;
	}

	public void setRs(List<RangeSearch> ranges) {
		this.rs = ranges;
	}

	public List<CommonSearch> getS() {
		return s;
	}

	public void sets(List<CommonSearch> commons) {
		this.s = commons;
	}

	public int getSs() {
		return ss;
	}

	public void setSs(int ss) {
		this.ss = ss;
	}

	public List<String> getSorts() {
		return sorts;
	}

	public String getCurSort() {
		if (CollectionUtils.isEmpty(sorts)) {
			return "score_1";
		} else {
			return sorts.get(0);
		}
	}

	public void setSorts(List<String> sorts) {
		this.sorts = sorts;
	}

	public List<String> getDb() {
		return db;
	}

	public void setDb(List<String> db) {
		this.db = db;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}