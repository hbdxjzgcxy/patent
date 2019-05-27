package com.xdtech.patent.service;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.xdtech.patent.conf.AppConf;
import com.xdtech.util.BdbDedupl;
import com.xdtech.util.Dedupl;

/**
 * excel --> java Obj
 * @author Administrator
 *
 */
public class Raw {

	private static Map<String, String> header = new HashMap<String, String>();
	

	

	/**
	 * 处理专题名称字段为多值字段
	 * @param docNo
	 * @param tName
	 * @return
	 */
	static Dedupl dedupl = null;

	private String mutilName(String docNo, String tName) {
		File file = new File(this.getClass().getResource("/").getFile()); //获取classes所在的目录
		File webroot = file.getParentFile().getParentFile(); //定位到webroot
		if (dedupl == null)
			dedupl = BdbDedupl.get(AppConf.get().get("db.path", webroot + File.separator + "db"), true);

		docNo = DigestUtils.md5Hex(docNo);
		String tmp = dedupl.getRecord(docNo);//去重
		if (!StringUtils.isEmpty(tmp)) {
			if (tmp.indexOf(tName) == -1) {
				tName = tmp + "," + tName;
			} else
				tName = tmp;
		}

		dedupl.saveRecord(docNo, tName);
		return tName;
	}

	/*
	 * excel行数据 --> FullText
	 */
	public FullText toFullText(Map<String, String> data, int uid, String tName, String fileName, List<String> extFields) {
		FullText text = new FullText();

		String an = "申请号";
		String pn = "公开号";
		String pd = "公开日";

		LinkedHashMap<String, Object> syncDataMap = new LinkedHashMap<String, Object>();

		String anVal = data.get(an);
		String pnVal = data.get(pn);
		String pdVal = data.get(pd);

		text.setAn(anVal);
		text.setPn(pnVal);
		text.setPd(pdVal);

		// 申请号、公开号数据为空时自动过滤不处理,不记录日志。
		if (StringUtils.isEmpty(anVal) || StringUtils.isEmpty(pn)) {

		}

		String multiple = mutilName(text.getDocno(), tName);

		for (String key : header.keySet()) {
			if (StringUtils.isEmpty(key)) {
				continue;
			}
			text.setAd(data.get(header.get("AD")));
			text.setCdn(data.get(header.get("CDN")));
			text.setPfn(data.get(header.get("PFN")));
			text.setIpc(data.get(header.get("IPC")));
			text.setAu(data.get(header.get("AU")));
			text.setPa(data.get(header.get("PA")));
			text.setUid(String.valueOf(uid));
			text.setTi(data.get(header.get("TI")));
			text.setAb(data.get(header.get("AB")));
			text.setT_name(tName);

			if (CollectionUtils.isNotEmpty(extFields)) {
				for (int j = 0; j < extFields.size(); j++) {
					String field = extFields.get(j);
					syncDataMap.put("EXT_DISPLAY_" + (j + 1), field);
				}
			}

			for (int i = 0; i < fileName.split("-").length; i++) {
				syncDataMap.put("CLASS_" + (i + 1), fileName.split("-")[i]);
			}

			text.setLaw(data.get(header.get("LAW")));
		}

		return text;
	}

	static {
		header.put("TI", "标题");
		header.put("AB", "摘要");
		header.put("AC", "专利申请国/地区/组织");
		header.put("ADDR", "申请（专利权）人地址");
		header.put("AU", "发明人");
		header.put("PA", "申请人");
		header.put("AN", "申请号");
		header.put("AD", "申请日");
		header.put("PN", "公开号");
		header.put("PD", "公开号");
		header.put("PR", "优先权");
		header.put("PRC", "优先权国家/地区");
		header.put("PRD", "优先权日");
		header.put("PRN", "优先权申请号");
		header.put("CLM", "权利要求");
		header.put("IPC", "IPC");
		header.put("PFN", "同族");
		header.put("CDN", "引证");
		header.put("LAW", "法律状态");

		header.put("AU_S", "扩展列");
		header.put("PA_S", "扩展列");
		header.put("AD_M_S", "扩展列");
		header.put("AD_Y_S", "扩展列");
		header.put("PRD_M_S", "扩展列");
		header.put("PRD_Y_S", "扩展列");
		header.put("PD_M_S", "扩展列");
		header.put("PD_Y_S", "扩展列");
		header.put("IPC_B_S", "扩展列");
		header.put("IPC_DL_S", "扩展列");
		header.put("IPC_XL_s", "扩展列");
		header.put("IPC_DZ_S", "扩展列");
		header.put("PFN_COUNT", "扩展列");
		header.put("CDN_COUNT", "扩展列");
		header.put("LSE", "扩展列");
		header.put("LS", "扩展列");
		header.put("LSE", "扩展列");
		header.put("LE", "扩展列");
		header.put("LSD", "扩展列");
		header.put("LSD_Y_S", "扩展列");
		header.put("LSI", "扩展列");
		header.put("LSND", "扩展列");
		header.put("LSN", "扩展列");
		header.put("LSNE", "扩展列");
		header.put("LSNI", "扩展列");
		header.put("docNo", "扩展列");
		header.put("UID", "扩展列");
		header.put("CNLX", "扩展列");
	}

	public Set<String> getHeader() {
		return header.keySet();
	}

}
