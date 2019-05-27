package com.xdtech.patent.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xdtech.parser.ParseResult;
import com.xdtech.parser.PatentParser;
import com.xdtech.patent.entity.SearchHistory;
import com.xdtech.patent.ipc.IPCContext;
import com.xdtech.search.client.LOGIC;
import com.xdtech.search.client.QueryObjectBuilder;
import com.xdtech.search.client.ws.BooleanQuery;
import com.xdtech.search.client.ws.FacetsResult;
import com.xdtech.search.client.ws.QueryClause;

/**
 * 检索Action
 * 
 * @author changfei
 *
 */
@Controller
public class SearchAction extends AbstractBaseSeachAction {

	/**
	 * 进入智能检索首页
	 * 
	 * @return
	 */
	@RequestMapping("smart_search")
	public ModelAndView smartIndex() {
		ModelAndView mv = new ModelAndView("search/smart");
		if(currentUser.getRole().isAdmin()){
			mv.setViewName("redirect:adminCenter/index.html");
		}
		mv.addObject("cur", "smart");
		List<SearchHistory> list = service.findByPage("from SearchHistory where user=? order by time desc", 1, 10,
				new Object[] { currentUser.getId() });
		mv.addObject("slogs", list);
		return mv;
	}

	/**
	 * 搜索逻辑
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("search")
	public ModelAndView search(@ModelAttribute("params") SearchForm model, HttpServletRequest request) {

		ModelAndView mv = doSearch(model);

		mv.addObject("style", model.getStyle());

		mv.setViewName("search/result/" + model.getStyle());

		mv.addObject("f_t", model.getFacetTarget());

		if (model.getSs() == 0)
			model.setSs(2);

		log(model, mv, request);

		return mv;

	}

	/**
	 * 处理表格检索逻辑
	 * 
	 * @param model
	 * @param builder
	 */
	private void procTableSearch(SearchForm model, QueryObjectBuilder builder) {
		StringBuilder qstr = new StringBuilder("");
		boolean insert = false;

		/*
		 * kwd==null说明是第一检索,需要根表格条件构建检索表达式 。
		 *
		 */
		if (StringUtils.isEmpty(model.getKwd())) {
			List<String> fields = model.getFields(); // 选定的检索字段 TI/AB
			if (fields != null && fields.size() > 0) {
				/*
				 * 分号分隔的检索词全部包含在选定的字段
				 */
				String keywords = model.getMoreKwd();
				if (StringUtils.isNotEmpty(keywords)) {
					addSearch(qstr, fields, keywords, "AND");
				}

				/*
				 * 分号分隔的检索词至少有一个包含在选定的字段
				 */
				keywords = model.getLessKwd();
				if (StringUtils.isNotEmpty(keywords)) {
					insert = qstr.length() > 0;
					if (insert) {
						qstr.append(" AND(");
					}
					addSearch(qstr, fields, keywords, "OR");
					if (insert)
						qstr.append(") ");
				}

				/*
				 * 分号分隔的检索词都不能在要检索的字段中出现
				 */
				keywords = model.getNotKwd();
				if (StringUtils.isNotEmpty(keywords)) {
					insert = qstr.length() >= 0;
					if (insert) {
						qstr.append(" NOT(");
					}
					addSearch(qstr, fields, keywords, "OR");
					if (insert)
						qstr.append(") ");
				}

			}

			if (model.getS() != null) {
				StringBuilder sstr = new StringBuilder();
				for (CommonSearch s : model.getS()) {
					String q = s.getQuery();
					if (q != null) {
						if (sstr.length() > 0) {
							sstr.append(" AND ");
						}
						sstr.append(q);
					}
				}
				if (sstr.length() > 0) {
					if (qstr.length() > 0) {
						qstr.append(" AND (").append(sstr).append(")");
					} else {
						qstr = sstr;
					}
				}

			}

			if (model.getRs() != null) {
				StringBuilder rsstr = new StringBuilder();
				for (RangeSearch s : model.getRs()) {
					String q = s.getQuery();
					if (q != null) {
						if (rsstr.length() > 0) {
							rsstr.append(" AND ");
						}
						rsstr.append(q);
					}
				}
				if (rsstr.length() > 0) {
					if (qstr.length() > 0) {
						qstr.append(" AND (").append(rsstr).append(")");
					} else {
						qstr = rsstr;
					}
				}

			}

		} else {
			qstr = new StringBuilder(model.getKwd());
		}
		model.setKwd(qstr.toString());
		procSmartSearch(model, builder);
	}

	private void addSearch(StringBuilder qbuiler, List<String> fields, String keywords, String logic) {
		for (int i = 0; i < fields.size(); i++) {
			if (i > 0) {
				qbuiler.append(" OR");
			}
			qbuiler.append(" (");
			if(keywords.contains("，")){
				keywords = keywords.replace("，", ",");
			}
			String kwds[] = keywords.split(",");
				for (int j = 0; j < kwds.length; j++) {
					if (j > 0)
						qbuiler.append(" ").append(logic).append(" ");
					qbuiler.append(fields.get(i)).append("=").append("(").append(kwds[j]).append(")");
				}
			qbuiler.append(")");
		}
	}

	protected void log(SearchForm model, ModelAndView mv, HttpServletRequest request) {
		/*
		 * 检索日志写入，只写入第一次检索
		 */
		try {
			if (model.getPageNo() == 1 && StringUtils.isNotEmpty(model.getKwd())) {
				SearchHistory his = new SearchHistory();
				his.setWord(model.getKwd());
				his.setType(model.getSearchType());
				his.setTime(new Date());
				his.setUser(currentUser.getId());
				his.setIp(resolveIP(request));
				his.setId(currentUser.getId());
				his.setQuery(model.getQstr());
				his.setHitcount((long) mv.getModel().get("s_total"));
				String takeTimeStr = mv.getModel().get("s_time").toString();
				his.setTakeTime(takeTimeStr);
				service.save(his);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理智能检索逻辑
	 * 
	 * @param model
	 * @param builder
	 */
	private void procSmartSearch(SearchForm model, QueryObjectBuilder builder) {
		String kwd = model.getKwd();
		BooleanQuery bq = null;
		String queryString = kwd; 
		if (StringUtils.isNotEmpty(kwd)) {
			if (!kwdIsQuery(kwd)) {
				queryString = defaultQuery(kwd.trim());
			}

			if (model.getSs() == 1) {
				String pkwd = model.getPkwd();
				if (StringUtils.isNotEmpty(pkwd)) {
					queryString = "( " + queryString + " ) AND (" + pkwd + ")";
				}
			}
			model.setQstr(queryString); //回显查询条件
		} else {
			StringBuilder queryStr = new StringBuilder();
			if (!StringUtils.isEmpty(model.gettName()) && !"all".equals(model.gettName())) {
				queryStr.append("T_NAME").append("=(").append(model.gettName()).append(")");
			} else {
				if (queryStr.length() == 0) {
					queryStr.append("UID=").append(currentUser.getId());
				}
			}
			queryString += queryStr.toString();
		}

		ParseResult parseResult = PatentParser.parse(queryString.toString(), "TI");
		bq = parseResult.getQuery();
		String hlWorls = parseResult.getTerm();
		builder.setHightlightWord(hlWorls);
		model.setHlKwd(hlWorls);
		builder.setQuery(bq);
	}

	/**
	 * 高级检索
	 * 
	 * @return
	 */
	@RequestMapping("advance_search")
	public ModelAndView advanceIndex() {
		ModelAndView mv = new ModelAndView("search/advance");
		mv.addObject("cur", "advance");
		return mv;
	}

	/**
	 * 高级检索
	 * 
	 * @return
	 */
	@RequestMapping("table_search")
	public ModelAndView tableIndex() {
		ModelAndView mv = new ModelAndView("search/table");
		mv.addObject("cur", "table");
		return mv;
	}

	@Override
	public void setupQuery(SearchForm model, ModelAndView mv, QueryObjectBuilder builder) {
		if ("smart".equals(model.getSearchType())) {
			procSmartSearch(model, builder);
			mv.addObject("cur", "smart");
		} else if ("table".equals(model.getSearchType())) {
			mv.addObject("cur", "table");
			procTableSearch(model, builder);
		} else if ("advanced".equals(model.getSearchType())) {
			procSmartSearch(model, builder);
			mv.addObject("cur", "advance");
		} else if ("ipc".equals(model.getSearchType())) {
			if (model.getSs() != 0) {
				procSmartSearch(model, builder);
			} else {
				procIPCSearch(model, builder);
			}
			mv.addObject("cur", "ipc");
		} else {
			procSmartSearch(model, builder);
		}
	}

	private void procIPCSearch(SearchForm model, QueryObjectBuilder builder) {
		String qstr = null;
		model.setKwd(model.getKwd().toUpperCase());
		if (!kwdIsQuery(model.getKwd())) {
			String[] IPCs = model.getKwd().split(" ");
			StringBuilder sb = new StringBuilder();
			for (String ipc : IPCs) {
				ipc = ipc.trim();
				if (!StringUtils.isEmpty(ipc)) {
					sb.append(IPCContext.category(ipc) + "=" + ipc).append(" ");
				}
			}
			qstr = sb.toString();
		} else {
			qstr = model.getKwd();
		}

		BooleanQuery bq = null;
		ParseResult parseResult = PatentParser.parse(qstr, "IPC");
		bq = parseResult.getQuery();
		builder.setQuery(bq);
		model.setKwd(qstr);
	}

	@Override
	protected void setupFilter(SearchForm model, ModelAndView mv, QueryObjectBuilder builder) {
		super.setupFilter(model, mv, builder);
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

	/**
	 * 根据业务设置分组统计条件
	 * 
	 * @param builder
	 */
	public void setupFacet(SearchForm model, QueryObjectBuilder builder) {
		builder.addFieldFacet("AC").addFieldFacet("LS").addFieldFacet("PA_S").addFieldFacet("CNLX")
				.addFieldFacet("MIPC_DL_S").addFieldFacet("MIPC_XL_S").addFieldFacet("PD_Y_S").addFieldFacet("AD_Y_S")
				.addFieldFacet("AU_S").addFieldFacet("T_NAME");
	}

	/**
	 * 处理检索结果返回的分组统计数据
	 */
	protected void proc(FacetsResult facets, ModelAndView mv) {
		if (facets != null) {
			mv.addObject("ac_facet_result", facets.getFieldFacets().get(0).getResult());
			mv.addObject("ls_facet_result", facets.getFieldFacets().get(1).getResult());
			mv.addObject("pa_facet_result", facets.getFieldFacets().get(2).getResult());
			mv.addObject("cnlx_facet_result", facets.getFieldFacets().get(3).getResult());
			mv.addObject("ipc_dl_facet_result", facets.getFieldFacets().get(4).getResult());
			mv.addObject("ipc_xl_facet_result", facets.getFieldFacets().get(5).getResult());
			mv.addObject("pdy_facet_result", facets.getFieldFacets().get(6).getResult());
			mv.addObject("ady_facet_result", facets.getFieldFacets().get(7).getResult());
			mv.addObject("au_facet_result", facets.getFieldFacets().get(8).getResult());
			mv.addObject("tn_facet_result", facets.getFieldFacets().get(9).getResult());
		}
	}

}
