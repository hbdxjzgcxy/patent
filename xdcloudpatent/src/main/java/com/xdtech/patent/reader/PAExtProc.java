package com.xdtech.patent.reader;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

/**
 * 申请入扩展处理程序
 * @author changfei
 *
 */
class PAExtProc {
	public static List<String> getPA_Ext(String pa,Map<String, ImportCfg> cfgs) {
		String PA_S = "";
		if (pa != null) {
			String[] splitValues = Util.splitAndTrim("PA", pa,cfgs);
			PA_S = Util.jionArray(splitValues, ";");
		}
		return Lists.newArrayList(PA_S);
	}
}
