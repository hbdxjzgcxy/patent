package com.xdtech.patent.action;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.xdtech.patent.conf.AppConf;
import com.xdtech.search.client.LOGIC;
import com.xdtech.search.client.QueryObjectBuilder;
import com.xdtech.search.client.XDSearchSupport;
import com.xdtech.search.client.util.ClientUtil;
import com.xdtech.search.client.ws.BooleanQuery;
import com.xdtech.search.client.ws.FacetsResult;
import com.xdtech.search.client.ws.Highlight;
import com.xdtech.search.client.ws.QueryClause;
import com.xdtech.search.client.ws.SDocument;
import com.xdtech.search.client.ws.SField;
import com.xdtech.search.client.ws.SearchResult;

/**
 * 检索框架抽象类
 * 
 * @author changfei
 *
 */
abstract public class AbstractBaseSeachAction extends BaseAction {

	/**
	 * 模板方法。专利分析、检索、下载、收藏都可执行此方法。根据不同的业务覆盖setupXXX方法和procXXX方法。
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	public final ModelAndView doSearch(@ModelAttribute("params") SearchForm model) {

		ModelAndView mv = new ModelAndView();
		QueryObjectBuilder builder = QueryObjectBuilder.newInstance().enableLog();

		setupCore(model, builder);
		setupQuery(model, mv, builder);
		setupFilter(model, mv, builder);
		setupReturnField(builder);
		setupSortFiled(model, builder);

		setupPage(model, builder);

		if (model.getFacet() == 1) {
			setupFacet(model, builder);
		}

		SearchResult result = search(builder);
		procSearchResult(model, mv, result);

		return mv;
	}

	protected void setupPage(SearchForm model, QueryObjectBuilder builder) {
		builder.setPage((model.getPageNo() - 1) * model.getPageSize(), model.getPageSize());
	}

	/**
	 * 设置分析（Facet）参数
	 * 
	 * @param form
	 * @param builder
	 */
	protected void setupGraphFacet(AnalysisForm form, QueryObjectBuilder builder) {

	}

	protected void setupCore(SearchForm model, QueryObjectBuilder builder) {
		builder.addCore(CORE_NAME);
	}

	protected void setupFacet(SearchForm form, QueryObjectBuilder builder) {

	}

	/**
	 * 根据业务要求设置排序条件
	 * 
	 * @param model
	 * @param builder
	 */
	protected void setupSortFiled(SearchForm model, QueryObjectBuilder builder) {
		List<String> sorts = model.getSorts();
		if (CollectionUtils.isNotEmpty(sorts)) {
			for (String field_reverse : sorts) {
				int mark = field_reverse.lastIndexOf("_");
				String field = field_reverse;
				boolean reverse = true;
				if (mark != -1) {
					field = field_reverse.substring(0, mark);
					reverse = "1".equals(field_reverse.substring(mark + 1));
				}
				builder.addSortField(field, reverse);
			}
			// builder.addSortField("score", false); //默认按相关度降序排序
		}
	}

	/**
	 * 设置检索结果要返回的字段
	 * 
	 * @param builder
	 */
	protected void setupReturnField(final QueryObjectBuilder builder) {
		builder.addReturnField("TI", true, hl(50)).addReturnField("AB", true, hl(200)).addReturnField("LS", false, hl(100))
				.addReturnField("AN", false, hl(100)).addReturnField("PA", true, hl(100)).addReturnField("AD", false, 100)
				.addReturnField("PN", false, 100).addReturnField("PD", false, 100).addReturnField("ADDR", false, 100).addReturnField("IPC", true, 100)
				.addReturnField("AGT", false, 100).addReturnField("AGC", false, 100).addReturnField("AU", false, 100).addReturnField("LS", false, 100)
				.addReturnField("CLM", false, 100).addReturnField("PFN", false, 100).addReturnField("FT", false, 100)
				.addReturnField("CTN", false, 100).addReturnField("CDN", false, 100).addReturnField("LSN", false, 100)
				.addReturnField("LSE", false, 100).addReturnField("PR", true, 100).addReturnField("PC", true, 100).addReturnField("PRC", true, 100)
				.addReturnField("PRD", false, 100).addReturnField("PRN", false, 100);

	}

	/**
	 * 根据业务设置过滤器
	 * 
	 * @param model
	 * @param mv
	 * @param builder
	 */
	protected void setupFilter(SearchForm model, ModelAndView mv, QueryObjectBuilder builder) {
		/*
		 * 检查用户是否选中子专题库
		 */
		String paths = model.getPath();
		if (!StringUtils.isEmpty(paths)) {
			builder.addFilter(newBooleanQ(newQueryClause(newTermQuery("path", paths + "*"), LOGIC.AND)));
		}

		if (!StringUtils.isEmpty(model.gettName()) && !"all".equals(model.gettName())) {
			builder.addFilter(newBooleanQ(newQueryClause(newTermQuery("T_NAME", model.gettName()), LOGIC.AND)));
		}

		/*
		 * 国别限制 中国/其他/非中国
		 */
		List<String> acList = model.getAC();
		if (CollectionUtils.isNotEmpty(acList)) {
			BooleanQuery bq = new BooleanQuery();
			for (String ac : acList) {
				bq.getClasuses().add(newQueryClause(newTermQuery("AC", wrap(ac)), LOGIC.OR));
			}
			builder.addFilter(bq);
			mv.addObject("cn_dt", "cn_dt");
		}

		/*
		 * 法律状态查询
		 */
		List<String> lsList = model.getLS();
		if (CollectionUtils.isNotEmpty(lsList)) {
			BooleanQuery bq = new BooleanQuery();
			for (String ls : lsList) {
				bq.getClasuses().add(newQueryClause(newTermQuery("LS", wrap(ls)), LOGIC.OR));
			}
			builder.addFilter(bq);
			mv.addObject("ls_dt", "ls_dt");
		}

		/*
		 * 申请人查询
		 */
		List<String> pas = model.getPA_S();
		if (CollectionUtils.isNotEmpty(pas)) {
			BooleanQuery bq = new BooleanQuery();
			for (String pa_s : pas) {
				bq.getClasuses().add(newQueryClause(newTermQuery("PA_S", wrap(pa_s)), LOGIC.OR));
			}
			builder.addFilter(bq);
			mv.addObject("pa_dt", "pa_dt");
		}

		/*
		 * 申请人查询
		 */
		List<String> aus = model.getAU_S();
		if (CollectionUtils.isNotEmpty(aus)) {
			BooleanQuery bq = new BooleanQuery();
			for (String au : aus) {
				bq.getClasuses().add(newQueryClause(newTermQuery("AU_S", wrap(au)), LOGIC.OR));
			}
			builder.addFilter(bq);
			mv.addObject("au_dt", "au_dt");
		}

		/*
		 * 专利类型查询
		 */
		List<String> cnlxList = model.getCNLX();
		if (CollectionUtils.isNotEmpty(cnlxList)) {
			BooleanQuery bq = new BooleanQuery();
			for (String cnlx : cnlxList) {
				bq.getClasuses().add(newQueryClause(newTermQuery("CNLX", wrap(cnlx)), LOGIC.OR));
			}
			builder.addFilter(bq);
			mv.addObject("cnlx_dt", "cnlx_dt");
		}

		List<String> adyList = model.getAD_Y_S();
		if (CollectionUtils.isNotEmpty(adyList)) {
			BooleanQuery bq = new BooleanQuery();
			for (String ad_y_s : adyList) {
				bq.getClasuses().add(newQueryClause(newTermQuery("AD_Y_S", wrap(ad_y_s)), LOGIC.OR));
			}
			builder.addFilter(bq);
			mv.addObject("adys_dt", "adys_dt");
		}

		List<String> pdyList = model.getPD_Y_S();
		if (CollectionUtils.isNotEmpty(pdyList)) {
			BooleanQuery bq = new BooleanQuery();
			for (String pd_y_s : pdyList) {
				bq.getClasuses().add(newQueryClause(newTermQuery("PD_Y_S", wrap(pd_y_s)), LOGIC.OR));
			}
			builder.addFilter(bq);
			mv.addObject("pdys_dt", "pdys_dt");
		}

		List<String> ipcdlList = model.getMIPC_DL_S();
		if (CollectionUtils.isNotEmpty(ipcdlList)) {
			BooleanQuery bq = new BooleanQuery();
			for (String ipc_dl : ipcdlList) {
				bq.getClasuses().add(newQueryClause(newTermQuery("MIPC_DL_S", wrap(ipc_dl)), LOGIC.OR));
			}
			builder.addFilter(bq);
			mv.addObject("ipcdl_dt", "ipcdl_dt");
		}

		List<String> ipcxlList = model.getMIPC_XL_S();
		if (CollectionUtils.isNotEmpty(ipcxlList)) {
			BooleanQuery bq = new BooleanQuery();
			for (String ipc_xl : ipcxlList) {
				bq.getClasuses().add(newQueryClause(newTermQuery("MIPC_XL_S", wrap(ipc_xl)), LOGIC.OR));
			}
			builder.addFilter(bq);
			mv.addObject("ipcxl_dt", "ipcxl_dt");
		}

		/*
		 * 限定检索范围为当前用户
		 */
		QueryClause UID = newQueryClause(newTermQuery("UID", getSearchId()), LOGIC.AND);
		builder.addFilter(newBooleanQ(UID));

	}

	protected String wrap(String kwd) {
		return "\"" + kwd + "\"";
	}

	/**
	 * 处理检索结果返回的分组统计数据
	 */
	protected void proc(FacetsResult facets, ModelAndView mv) {

	}

	/**
	 * 处理检索结果和页面适配
	 * 
	 * @param model
	 * @param mv
	 * @param result
	 */
	protected void procSearchResult(SearchForm model, ModelAndView mv, SearchResult result) {
		// 处理通结果
		if (model.getFacet() == 1)
			proc(result.getFacets(), mv);

		/*
		 * 转换检索结果集
		 */
		mv.addObject("s_time", result.getTime() / 1000.0f);
		mv.addObject("s_total", result.getTotal());
		mv.addObject("s_list", toResultBeanList(result.getDocuments(), SearchBean.class, true));
	}

	protected String resolveIP(HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 处理检索表达式，不同的业务必须实现该方法。
	 * 
	 * @param model
	 * @param mv
	 * @param builder
	 */
	abstract public void setupQuery(SearchForm model, ModelAndView mv, QueryObjectBuilder builder);

	/**
	 * 默认查询
	 * 
	 * @param kwd
	 * @return
	 */
	protected String defaultQuery(String kwd) {
		StringBuilder strb = new StringBuilder();
		strb.append("TI=(").append(kwd).append(") ");
		strb.append("AB=(").append(kwd).append(") ");
		// strb.append("AN=(").append(kwd).append(") ");
		// strb.append("PN=(").append(kwd).append(") ");
		// strb.append("LS=(").append(kwd).append(") ");
		return strb.toString();
	}

	private String wsdl = AppConf.get().get("search.servcie.wsdl", "http://192.168.1.197:8080/xdcloudsearch/service/search?wsdl");

	private XDSearchSupport support = null;

	protected Highlight hl(String tag, int len) {
		return ClientUtil.newHighlight("<" + tag + " color='red'>", "</" + tag + ">", len);
	}

	protected Highlight hl(int len) {
		return hl("font", len);
	}

	public SearchResult search(QueryObjectBuilder builder) {
		return getSupport().search(builder);
	}

	protected XDSearchSupport getSupport() {
		if (support == null) {
			try {
				support = new XDSearchSupport(wsdl);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return support;
	}

	public String getDocNo(int uid, String docNo) {
		if (docNo.indexOf("!") > -1)
			return docNo;
		return getSearchId() + "!" + docNo;
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> toResultBeanList(List<SDocument> docList, Class<? extends SearchBean> clzz, boolean enableSummary) {
		if (docList == null || docList.isEmpty()) {
			return new ArrayList<T>(0);
		}
		List<T> list = new ArrayList<T>(docList.size());
		if (docList != null) {
			for (SDocument doc : docList) {
				Object bean = null;
				try {
					bean = clzz.newInstance();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
				List<SField> fieldList = doc.getFields();
				for (SField field : fieldList) {
					String name = field.getField();
					try {
						List<String> values = field.getValues();
						String value = "";
						if (values != null) {
							value = values.toString();
							value = value.substring(1, value.length() - 1);
						}
						BeanUtils.setProperty(bean, name, value);
					} catch (Exception e) {
						// e.printStackTrace();
					}
				}
				SearchBean searchBean = (SearchBean) bean;
				searchBean.setScore(doc.getScore());
				searchBean.setDocNo(doc.getDocNo());
				if (enableSummary) {
					searchBean.setSummary(doc.getSummaries());
				}
				list.add((T) bean);
			}
		}
		return list;
	}

	/**
	 * 检测用户输入的关键词是否是类SQL表达式
	 * 
	 * @param kwd
	 * @return
	 */
	protected boolean kwdIsQuery(String kwd) {
		return kwd.indexOf("=") != -1 || kwd.indexOf(">") != -1 || kwd.indexOf("<") != -1;
	}

	public String getSearchId() {
		int tid = currentUser.getId();
		if (currentUser.getPid() != 0) {
			tid = currentUser.getPid();
		}
		return Integer.toString(tid);
	}
}
