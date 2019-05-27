package com.xdtech.patent.action;

import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.xdtech.patent.ipc.IPCContext;
import com.xdtech.patent.ipc.IPCTreeNode;
import com.xdtech.search.client.LOGIC;
import com.xdtech.search.client.QueryObjectBuilder;
import com.xdtech.search.client.XDIndexOperationSupport;
import com.xdtech.search.client.ws.BooleanQuery;
import com.xdtech.search.client.ws.SearchResult;
import com.xdtech.search.client.ws.StringArray;
import com.xdtech.search.client.ws.TermQuery;
import com.xdtech.search.client.ws.XDCloudSearchException_Exception;

@RequestMapping("/ipc")
@Controller
public class IPCAction extends AbstractBaseSeachAction {

	@RequestMapping("index")
	public ModelAndView ipc() {
		ModelAndView mv = new ModelAndView("search/ipc");
		mv.addObject("cur", "ipc");
		return mv;
	}

	/**
	 * 分类检索
	 * 
	 * @return
	 */
	@RequestMapping("/category")
	public ModelAndView ipcCategory(@ModelAttribute("codes") String codes, @RequestParam(value = "code") String code) {
		ModelAndView mv = new ModelAndView("/ipc_result");
		IPCTreeNode ipc = IPCContext.get(code.toUpperCase());
		mv.addObject("ipcCategory", ipc.getChildren());
		mv.addObject("cur", "ipc");

		if (!StringUtils.isEmpty(codes) && StringUtils.indexOf(codes, code) != -1) {
			codes = StringUtils.substringBefore(codes, "," + code);
		}
		codes = codes + "," + code;

		mv.addObject("codes", codes);
		return mv;
	}

	@Override
	protected void setupCore(SearchForm model, QueryObjectBuilder builder) {
		builder.addCore("ipc");
	}

	@RequestMapping("search")
	public ModelAndView search(String kwd) {
		ModelAndView mv = new ModelAndView("search/ipc");
		mv.addObject("cur", "ipc");
		QueryObjectBuilder builder = QueryObjectBuilder.newInstance();
		builder.addCore("ipc");
		TermQuery codeTQ = newTermQuery("code", escape(kwd) + "*");
		TermQuery descTQ = newTermQuery("desc", escape(kwd));
		BooleanQuery query = newBooleanQ(newQueryClause(codeTQ, LOGIC.OR), newQueryClause(descTQ, LOGIC.OR));
		builder.setQuery(query);
		builder.addReturnField(newReturnField("code", true, 80)).addReturnField(newReturnField("desc", true, 80));
		builder.addSortField(newSortField("code", false));
		builder.setPage(0, 100);
		builder.setHightlightWord(kwd);
		SearchResult result = search(builder);
		List<IPCBean> s_list = toResultBeanList(result.getDocuments(), IPCBean.class, false);
		mv.addObject("s_list", s_list);
		return mv;

	}

	

	@Override
	public void setupQuery(SearchForm model, ModelAndView mv, QueryObjectBuilder builder) {
		
		
	}

}
