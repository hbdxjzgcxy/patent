package com.xdtech.patent.reader;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

class AUExtProc {
	public static List<String> getAU_Ext(String cellVal,Map<String, ImportCfg> cfgs) {
		return Lists.newArrayList(Util.proc("AU", cellVal,cfgs));
	}
}
