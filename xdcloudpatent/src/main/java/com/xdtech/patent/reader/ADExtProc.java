package com.xdtech.patent.reader;

import java.util.List;

import com.google.common.collect.Lists;

public class ADExtProc {
	
	/**
	 * AD 字段相关扩展
	 * @param adVal
	 * @return
	 */
	 static List<String> getAD_Ext(String adVal) {
		String AD_Y_S = "", AD_M_S = "";
		if (adVal.length() >= 4) {
			AD_Y_S = adVal.substring(0, 4);
		}
		if (adVal.length() >= 6) {
			AD_M_S = adVal.substring(0, 6);
		}
		return Lists.newArrayList(AD_Y_S, AD_M_S);
	}

	

}
