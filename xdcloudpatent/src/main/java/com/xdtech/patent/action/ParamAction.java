package com.xdtech.patent.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xdtech.patent.entity.ParamInfo;
import com.xdtech.patent.service.ParamService;
@Controller
public class ParamAction extends BaseAction{

	@Autowired
	private ParamService paramService;
	
	@RequestMapping("/param/save")
	public @ResponseBody String saveParam(ParamInfo param){
		String msg = "success";
		if(null == param.getId() || "".equals(param.getId())){
			paramService.save(param);
		}else{
			paramService.update(param);
		}
		ParamContext.setParamInfo(param);
		ParamContext.reload();
		return msg;
	}
	
}
