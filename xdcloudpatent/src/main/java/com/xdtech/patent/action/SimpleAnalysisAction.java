package com.xdtech.patent.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SimpleAnalysisAction extends AnalysisAction {

	/**
	 * 根据检索结果返回分析结果
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("analysis")
	public ModelAndView analysis(@ModelAttribute("analysiForm") SearchForm form) {
		form.setPageSize(1);
		form.setFacet(1);
		ModelAndView mv = doSearch(form);
		mv.setViewName("analysis/index");
		mv.addObject("cur", "analysis");
		// mv.addObject("s_total", result.getTotal());
		log("analysis", "分析");
		return mv;
	}
	
}
