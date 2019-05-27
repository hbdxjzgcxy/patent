package com.xdtech.patent.reader;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

public class PNExtProc {
	/**
	 * PN字段相关扩展
	 * @param cellVal
	 * @return
	 */
	static List<String> getPN_Ext(String cellVal) {
		String AC = "", CNLX = "";
		if (cellVal.length() > 2) {
			AC = cellVal.substring(0, 2);
		}
		String pn = cellVal.replace(" ", "");//CN88100001A/CN 88100001 A都是正确格式
		String flag = null;
		if (pn.length() == 11) {
			//1985-1988
			flag = StringUtils.substring(pn, 4, 5);
		} else {
			//历次编号体系的修改中，申请号变动较大，公开号只是位数加大结构未有较大变化。
			flag = StringUtils.substring(pn, 2, 3);
		}
		if ("CN".equals(AC)) {
			if ("3".equals(flag))
				CNLX = "外观设计";
			else if ("2".equals(flag))
				CNLX = "实用新型";
			else if ("1".equals(flag))
				CNLX = "发明";
		}
		return Lists.newArrayList(AC, CNLX);
	}
}
