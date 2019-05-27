package com.xdtech.patent.reader;

import java.util.List;

import com.google.common.collect.Lists;

public class PDExtProc {
	
	/**
	 * PD字段相关扩展
	 * @param pdVal
	 * @return
	 */
	 static List<String> getPD_Ext(String pdVal) {
		String PD_Y_S = "", PD_M_S = "";
		if (pdVal.length() >= 4) {
			PD_Y_S = pdVal.substring(0, 4);
		}
		if (pdVal.length() >= 6) {
			PD_M_S = pdVal.substring(0, 6);
		}
		return Lists.newArrayList(PD_Y_S, PD_M_S);
	}

}
