package com.xdtech.patent.action;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xdtech.search.client.QueryObjectBuilder;
import com.xdtech.search.client.ws.XDCloudSearchException_Exception;

import net.sf.json.JSONArray;

@Controller
public class SuggestAction extends AbstractBaseSeachAction{

	@RequestMapping("/complete")
	public @ResponseBody String autocomplete(@RequestParam(value="term")String kwd) throws XDCloudSearchException_Exception{
		List<String> hotwords = getSupport().getSearchService().findHotWord(kwd,5);
		String result = JSONArray.fromObject(hotwords).toString();
		System.out.println(result);
		return result;
	}

	@Override
	public void setupQuery(SearchForm model, ModelAndView mv, QueryObjectBuilder builder) {
		// TODO Auto-generated method stub
		
	}

}
