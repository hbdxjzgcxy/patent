package com.xdtech.patent.reader;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

/**
 * IPC 扩展处理程序
 * 
 * @author changfei
 *
 */
class IPCExtProc {
	/**
	 * IPC字段相关扩展
	 * 
	 * @param ipc
	 * @return
	 */
	static List<String> getIPC_Ext(String ipc,Map<String, ImportCfg> cfgs) {
		// "IPC_B_S", "IPC_DL_S", "IPC_XL_S", "IPC_DZ_S", "MIPC", "MIPC_B_S",
		// "MIPC_DL_S","MIPC_DZ_S", "MIPC_XL_S", "MIPC_XZ_S"
		String ipcs[] = Util.splitAndTrim("IPC", ipc,cfgs);
		String IPC_B_S = "";
		String IPC_DL_S = "";
		String IPC_XL_S = "";
		String IPC_DZ_S = "";
		String MIPC = "";
		String MIPC_B_S = "";
		String MIPC_DL_S = "";
		String MIPC_XL_S = "";
		String MIPC_DZ_S = "";
		if (ipcs != null && ipcs.length > 0) {
			MIPC = ipcs[0];

			// 0-1
			Set<String> set = Util.getHeadStr(ipcs, 0, 1);
			if (!set.isEmpty()) {
				MIPC_B_S = set.iterator().next();
				IPC_B_S = Util.joinSet(set, PatentReader.JOIN_CHAR);
			}

			// 0-3
			set = Util.getHeadStr(ipcs, 0, 3);
			if (!set.isEmpty()) {
				MIPC_DL_S = set.iterator().next();
				IPC_DL_S = Util.joinSet(set, PatentReader.JOIN_CHAR);
			}

			// 0-4
			set = Util.getHeadStr(ipcs, 0, 4);
			if (!set.isEmpty()) {
				MIPC_XL_S = set.iterator().next();
				IPC_XL_S = Util.joinSet(set, PatentReader.JOIN_CHAR);
			}

			// 去掉分类号以“/”分隔的部分
			set = getIpc_dz_s(ipcs);
			if (!set.isEmpty()) {
				MIPC_DZ_S = set.iterator().next();
				IPC_DZ_S = Util.joinSet(set, PatentReader.JOIN_CHAR);
			}
		}
		return Lists.newArrayList(IPC_B_S, IPC_DL_S, IPC_XL_S, IPC_DZ_S, MIPC, MIPC_B_S, MIPC_DL_S, MIPC_DZ_S, MIPC_XL_S);
	}

	private static Set<String> getIpc_dz_s(String[] ipc) {
		Set<String> b = new LinkedHashSet<String>();
		for (String ipcStr : ipc) {
			ipcStr = ipcStr.replace(" ", "").trim();
			if (!ipcStr.isEmpty()) {
				if (ipcStr.indexOf("/") > -1) {
					b.add(StringUtils.substringBefore(ipcStr, "/"));
				} else {
					// B61D002500 国外专利号
					String main = StringUtils.substring(ipcStr, 0, 4);
					String zz = StringUtils.substring(ipcStr, 4);
					if (zz.length() == 6) {
						zz = StringUtils.substring(zz, 2, 4);
					} else if (zz.length() == 4) {
						zz = StringUtils.substring(zz, 0, 2);
					}
					b.add(main + zz);
				}
			}
		}
		return b;
	}

}