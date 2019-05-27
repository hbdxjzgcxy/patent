package com.xdtech.patent.reader;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

class LSEExtProc {
	static List<String> getLSE_Ext(String law) {
		String /*LS = "",*/ LE = "", LSD = "", LSN = "", LSD_Y_S = "", LSI = "", LSND = "", LSNE = "", LSNI = "";
		List<Map<String, String>> laws = analyticalLegalStatus(law);
		if (!CollectionUtils.isEmpty(laws)) {
			Map<String, String> latestEvent = laws.get(laws.size() - 1);

			if (latestEvent != null && latestEvent.size() > 0) {
				/*LS = CNPatentProcessTemplate.legalStatus(latestEvent.get("法律状态"));*/
				LE = latestEvent.get("法律状态");
				LSD = latestEvent.get("法律状态公告日");
				LSD_Y_S = StringUtils.substring(LSD, 0, 4);

				StringBuilder sb = new StringBuilder();
				for (String k : latestEvent.keySet()) {
					sb.append(k).append(":").append(latestEvent.get(k)).append(",");
				}
				LSI = StringUtils.substringBefore(sb.toString(), ",");
				LSND = LSD;
				LSN = LE;
				LSNE = latestEvent.get("描述信息");
				LSNI = LSNE;
			}
		}
		return Lists.newArrayList(/*LS, */LE, LSD, LSN, LSD_Y_S, LSI, LSND, LSNE, LSNI);
	}

	/**
	 * 解析法律状态
	 * @param legal
	 * @return
	 */
	private static List<Map<String, String>> analyticalLegalStatus(String legal) {
		String separator = "法律状态公告日";
		if (!StringUtils.isEmpty(legal)) {
			if (StringUtils.indexOf(legal, separator) != -1) {
				String[] items = legal.split(separator);
				List<Map<String, String>> result = new LinkedList<Map<String, String>>();
				for (String item : items) {
					String[] str = StringUtils.split(item, ";");
					if (str != null && str.length > 0) {
						Map<String, String> map = new LinkedHashMap<String, String>();
						for (String s : str) {
							s = s.replace(" ", "").replace("：", ":").trim();
							String key = StringUtils.substringBefore(s, ":");
							String val = StringUtils.substringAfter(s, ":");
							if (StringUtils.isEmpty(key))
								key = separator;
							map.put(key, val);
						}
						result.add(map);
					}
				}
				return result;
			}
		}
		return null;
	}

}
