package com.xdtech.patent.reader;
import java.util.List;

import com.google.common.collect.Lists;

public class PRDExtProc {
	
	/**
	 * PRD 字段相关扩展
	 * @param prdVal
	 * @return
	 */
	 static List<String> getPRD_Ext(String prdVal) {
		String PRD_Y_S = "", PRD_M_S = "";
		if (prdVal.length() >= 4) {
			PRD_Y_S = prdVal.substring(0, 4);
		}
		if (prdVal.length() >= 6) {
			PRD_M_S = prdVal.substring(0, 6);
		}
		return Lists.newArrayList(PRD_Y_S, PRD_M_S);
	}

	

}
