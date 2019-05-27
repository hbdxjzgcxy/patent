package com.xdtech.patent.reader;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

/**
 * CPC扩展处理程序
 * @author changfei
 *
 */
public class CPCExtProc {
	/**
	 * CPC字段相关扩展
	 * @param ipc
	 * @return
	 */
	static List<String> getCPC_Ext(String ipc,Map<String, ImportCfg> cfgs) {
		String ipcs[] = Util.splitAndTrim("IPC", ipc,cfgs);
		String CPC_B_S = "";
		String CPC_DL_S = "";
		String CPC_XL_S = "";
		String CPC_DZ_S = "";
		if (ipcs != null && ipcs.length > 0) {

			// 0-1
			Set<String> set = Util.getHeadStr(ipcs, 0, 1);
			if (!set.isEmpty()) {
				CPC_B_S = Util.joinSet(set, PatentReader.JOIN_CHAR);
			}

			//0-3
			set = Util.getHeadStr(ipcs, 0, 3);
			if (!set.isEmpty()) {
				CPC_DL_S = Util.joinSet(set, PatentReader.JOIN_CHAR);
			}

			//0-4
			set = Util.getHeadStr(ipcs, 0, 4);
			if (!set.isEmpty()) {
				CPC_XL_S = Util.joinSet(set, PatentReader.JOIN_CHAR);
			}

			//去掉尾部
			set = getCPC_dz_s(ipcs);
			if (!set.isEmpty()) {
				CPC_DZ_S = Util.joinSet(set, PatentReader.JOIN_CHAR);
			}
		}

		return Lists.newArrayList(CPC_B_S, CPC_DL_S, CPC_XL_S, CPC_DZ_S);
	}

	private static Set<String> getCPC_dz_s(String[] ipc) {
		Set<String> b = new LinkedHashSet<String>();
		for (String ipcStr : ipc) {
			ipcStr = ipcStr.replace(" ", "").trim();
			b.add(StringUtils.substringBefore(ipcStr, "/"));
		}
		return b;
	}

}
