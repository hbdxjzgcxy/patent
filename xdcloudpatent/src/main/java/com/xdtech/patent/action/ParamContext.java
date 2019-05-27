package com.xdtech.patent.action;

import java.util.List;

import com.xdtech.patent.conf.AppContextUtils;
import com.xdtech.patent.entity.ParamInfo;
import com.xdtech.patent.service.CommonService;

public class ParamContext {

	private static ParamInfo param;

	public static ParamInfo getParamInfo() {
		return param;
	}

	public static void setParamInfo(ParamInfo paramInfo) {
		param = paramInfo;
	}

	public static void init() {
		CommonService service = AppContextUtils.getBean("commonService");
		List<ParamInfo> paramInfo = service.findAll(ParamInfo.class);
		param = paramInfo.get(0);
	}

	public static void reload() {
		init();
	}

}
