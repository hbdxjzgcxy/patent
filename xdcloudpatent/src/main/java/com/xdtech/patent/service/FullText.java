package com.xdtech.patent.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.joda.time.DateTime;

import com.xdtech.search.client.ws.StringArray;

/**
 * @author sunjp
 *
 */
public class FullText{
	
	private static Map<String, String> ext = new HashMap<String, String>();//扩展列
	
	private String ti;//名称、标题
	private String ab;//摘要
	private String ac;//专利申请国/地区/组织
	private String addr;//申请（专利权）人地址
	
	private String au;//发明人
	private String au_s;
	
	private String pa;//申请人
	private String pa_s;
	
	private String an;//申请号
	
	private String ad;//申请日
	private String ad_m_s;
	private String ad_y_s;
	
	private String pn;//公开号
	
	private String pd;//公开日
	private String pd_m_s;
	private String pd_y_s;
	
	private String pr;//优先权
	private String clm;//权利要求
	private String ft;//说明书
	
	private String ipc;//IPC
	private String ipc_b_s;
	private String ipc_dl_s;
	private String ipc_xl_s;
	private String ipc_dz_s;
	
	private String pfn;//同族
	private String pfn_count;
	
	private String cdn;//引证
	private String cdn_count;
	
	private String t_name;
	
	/*
	 * 法律状态
	 */
	private String lse;
	private String ls;
	private String le;
	private String lsd;
	private String lsd_y_s;
	private String lsi;
	private String lsnd;
	private String lsn;
	private String lsne;
	private String lsni;
	
	
	
	private String docNo;
	private String uid;
	
	private String cnlx;
	
	public String getTi() {
		return ti;
	}
	public void setTi(String ti) {
		this.ti = ti;
	}
	public String getAb() {
		return ab;
	}
	public void setAb(String ab) {
		this.ab = ab;
	}
	public String getAc() {
		if(StringUtils.isNotEmpty(pn)){
			ac = StringUtils.substring(pn, 0, 2);
		}
		return ac;
	}
	public void setAc(String ac) {
		this.ac = ac;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getAu() {
		return au;
	}
	public void setAu(String au) {
		this.au = au;
	}
	public String getPa() {
		return pa;
	}
	public void setPa(String pa) {
		this.pa = pa;
	}
	public String getAn() {
		return an;
	}
	public void setAn(String an) {
		this.an = an;
	}
	public String getAd() {
		DateTime ldt = new DateTime(ad);
		ad = String.valueOf(ldt.toString("yyyyMMdd"));//hhmmssSSS
		return ad;
	}
	public void setAd(String ad) {
		this.ad = ad;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public String getPd() {
		DateTime ldt = new DateTime(pd);
		pd = String.valueOf(ldt.toString("yyyyMMdd"));//hhmmssSSS
		return pd;
	}
	public void setPd(String pd) {
		this.pd = pd;
	}
	public String getPr() {
		return pr;
	}
	public void setPr(String pr) {
		this.pr = pr;
	}
	public String getClm() {
		return clm;
	}
	public void setClm(String clm) {
		this.clm = clm;
	}
	
	public String getFt() {
		return ft;
	}
	public void setFt(String ft) {
		this.ft = ft;
	}
	public String getIpc() {
		return ipc;
	}
	public void setIpc(String ipc) {
		this.ipc = ipc;
	}
	public String getPfn() {
		return pfn;
	}
	public void setPfn(String pfn) {
		this.pfn = pfn;
	}
	public String getCdn() {
		return cdn;
	}
	public void setCdn(String cdn) {
		this.cdn = cdn;
	}
	public String getAu_s() {
		au_s = au;
		return au_s;
	}
	public void setAu_s(String au_s) {
		this.au_s = au_s;
	}
	public String getPa_s() {
		pa_s = pa;
		return pa_s;
	}
	public void setPa_s(String pa_s) {
		this.pa_s = pa_s;
	}
	public String getAd_m_s() {
		if(StringUtils.isNotEmpty(ad)){
			if(ad.indexOf("-") != -1){
				ad_m_s = ad.substring(0, ad.lastIndexOf("-")).replace("-", "");
			}
		}
		return ad_m_s;
	}
	public void setAd_m_s(String ad_m_s) {
		if(StringUtils.isNotEmpty(ad)){
			if(ad.indexOf("-") != -1){
				ad_y_s = ad.substring(0, ad.indexOf("-")).replace("-", "");
			}
		}
		this.ad_m_s = ad_y_s;
	}
	public String getAd_y_s() {
		return ad_y_s;
	}
	public void setAd_y_s(String ad_y_s) {
		this.ad_y_s = ad_y_s;
	}
	public String getPd_m_s() {
		if(StringUtils.isNotEmpty(pd)){
			if(pd.indexOf("-") != -1){
				pd_m_s = pd.substring(0, pd.lastIndexOf("-")).replace("-", "");
			}
		}
		return pd_m_s;
	}
	public void setPd_m_s(String pd_m_s) {
		this.pd_m_s = pd_m_s;
	}
	public String getPd_y_s() {
		if(StringUtils.isNotEmpty(pd)){
			if(pd.indexOf("-") != -1){
				pd_y_s = pd.substring(0, pd.indexOf("-")).replace("-", "");
			}
		}
		return pd_y_s;
	}
	public void setPd_y_s(String pd_y_s) {
		this.pd_y_s = pd_y_s;
	}
	public String getIpc_b_s() {
		Set<String> b = new LinkedHashSet<String>();
		if(!StringUtils.isNotEmpty(ipc)){
			for (String ipcStr : ipc.split(";")) {
				ipcStr = ipcStr.replace(" ", "");
				b.add(StringUtils.substring(ipcStr, 0, 1));
			}
		}
		ipc_b_s = StringUtils.isEmpty(StringUtils.join(b, ",")) ? "" : StringUtils.join(b, ",");
		return ipc_b_s;
	}
	public void setIpc_b_s(String ipc_b_s) {
		this.ipc_b_s = ipc_b_s;
	}
	public String getIpc_dl_s() {
		Set<String> b = new LinkedHashSet<String>();
		if(!StringUtils.isNotEmpty(ipc)){
			for (String ipcStr : ipc.split(";")) {
				ipcStr = ipcStr.replace(" ", "");
				b.add(StringUtils.substring(ipcStr, 0, 3));
			}
		}
		ipc_dl_s = StringUtils.isEmpty(StringUtils.join(b, ",")) ? "" : StringUtils.join(b, ",");
		return ipc_dl_s;
	}
	public void setIpc_dl_s(String ipc_dl_s) {
		this.ipc_dl_s = ipc_dl_s;
	}
	public String getIpc_xl_s() {
		Set<String> b = new LinkedHashSet<String>();
		if(!StringUtils.isNotEmpty(ipc)){
			for (String ipcStr : ipc.split(";")) {
				ipcStr = ipcStr.replace(" ", "");
				b.add(StringUtils.substring(ipcStr, 0, 4));
			}
		}
		ipc_xl_s = StringUtils.isEmpty(StringUtils.join(b, ",")) ? "" : StringUtils.join(b, ",");
		return ipc_xl_s;
	}
	public void setIpc_xl_s(String ipc_xl_s) {
		this.ipc_xl_s = ipc_xl_s;
	}
	public String getIpc_dz_s() {
		Set<String> b = new LinkedHashSet<String>();
		if(!StringUtils.isNotEmpty(ipc)){
			for (String ipcStr : ipc.split(";")) {
				ipcStr = ipcStr.replace(" ", "");
				b.add(StringUtils.substringBefore(ipcStr, "/"));
			}
		}
		ipc_xl_s = StringUtils.isEmpty(StringUtils.join(b, ",")) ? "" : StringUtils.join(b, ",");
		return ipc_dz_s;
	}
	public void setIpc_dz_s(String ipc_dz_s) {
		this.ipc_dz_s = ipc_dz_s;
	}
	public String getPfn_count() {
		if (StringUtils.isNotEmpty(pfn)) {
			String[] PFNArray = pfn.split(";");
			pfn_count = String.valueOf(PFNArray.length);
		}else{
			pfn_count = "0";
		}
		return pfn_count;
	}
	public void setPfn_count(String pfn_count) {
		this.pfn_count = pfn_count;
	}
	public String getCdn_count() {
		if (StringUtils.isNotEmpty(cdn)) {
			String[] CDNArray = cdn.split(";");
			cdn_count = String.valueOf(CDNArray.length);
		}else{
			cdn_count = "0";
		}
		return cdn_count;
	}
	public void setCdn_count(String cdn_count) {
		this.cdn_count = cdn_count;
	}
	
	
	
	public String getT_name() {
		return t_name;
	}
	public void setT_name(String t_name) {
		this.t_name = t_name;
	}
	public String getLse() {
		return lse;
	}
	public void setLse(String lse) {
		this.lse = lse;
	}
	public String getLs() {
		return ls;
	}
	public void setLs(String ls) {
		this.ls = ls;
	}
	public String getLe() {
		return le;
	}
	public void setLe(String le) {
		this.le = le;
	}
	public String getLsd() {
		return lsd;
	}
	public void setLsd(String lsd) {
		this.lsd = lsd;
	}
	public String getLsd_y_s() {
		return lsd_y_s;
	}
	public void setLsd_y_s(String lsd_y_s) {
		this.lsd_y_s = lsd_y_s;
	}
	public String getLsi() {
		return lsi;
	}
	public void setLsi(String lsi) {
		this.lsi = lsi;
	}
	public String getLsnd() {
		return lsnd;
	}
	public void setLsnd(String lsnd) {
		this.lsnd = lsnd;
	}
	public String getLsn() {
		return lsn;
	}
	public void setLsn(String lsn) {
		this.lsn = lsn;
	}
	public String getLsne() {
		return lsne;
	}
	public void setLsnt(String lsne) {
		this.lsne = lsne;
	}
	public String getLsni() {
		return lsni;
	}
	public void setLsni(String lsni) {
		this.lsni = lsni;
	}
	public String getDocno() {
		if (StringUtils.isEmpty(pd)) {
			docNo = uid + "!" + an + "_" + pn;
		} else {
			docNo = uid + "!" + an + "_" + pn + "_" + new DateTime(pd).toString("yyyyMMddhhmmssSSS");
		}
		return docNo;
	}
	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getCnlx() {
		if(StringUtils.isNotEmpty(pn)){
			cnlx = CNPatentProcessTemplate.applTypeCN(pn);
		}
		return cnlx;
	}
	public void setCnlx(String cnlx) {
		this.cnlx = cnlx;
	}
	
	public void setLaw(String law) {
		if (StringUtils.isNotEmpty(law)) {
			lse = CNPatentProcessTemplate.lawEvent(law);
			List<Map<String, String>> laws = CNPatentProcessTemplate.analyticalLegalStatus(law);
			if (!CollectionUtils.isEmpty(laws)) {
				Map<String, String> latestEvent = laws.get(laws.size() - 1);

				if (latestEvent != null && latestEvent.size() > 0) {

					ls = CNPatentProcessTemplate.legalStatus(latestEvent.get("法律状态"));
					le = latestEvent.get("法律状态");
					lsd = latestEvent.get("法律状态公告日");
					lsd_y_s = StringUtils.substring(lsd, 0, 4);

					StringBuilder sb = new StringBuilder();
					for (String k : latestEvent.keySet()) {
						sb.append(k).append(":").append(latestEvent.get(k)).append(",");
					}
					lsi = StringUtils.substringBefore(sb.toString(), ",");
					lsnd = lsd;
					lsn = le;
					lsne = latestEvent.get("描述信息");
					lsni = lsne;
				}
			}
		}
	}
	
	public StringArray toStringArray() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		if(ext == null || ext.isEmpty())ext();
		StringArray stringArray = new StringArray();
		for (String key : ext.keySet()) {
			String v = (String) MethodUtils.invokeMethod(this,"get"+StringUtils.capitalize(key.toLowerCase()),null);
			stringArray.getItem().add(v);
		}
		
		return stringArray; 
	}

	
	public static Set<String> header(){
		return ext.keySet();
	}
	
	private void ext(){
		ext.put("TI", "ti");
		ext.put("AB", "ab");
		ext.put("AC", "ac");
		ext.put("ADDR", "申请（专利权）人地址");
		ext.put("AU", "发明人");
		ext.put("PA", "申请人");
		ext.put("AN", "申请号");
		ext.put("AD", "申请日");
		ext.put("PN", "公开号");
		ext.put("PD", "公开号");
		ext.put("PR", "优先权");
		ext.put("CLM", "权利要求");
		ext.put("IPC", "IPC");
		ext.put("PFN", "同族");
		ext.put("CDN", "引证");
		
		ext.put("AU_S", "扩展列");
		ext.put("PA_S", "扩展列");
		ext.put("AD_M_S", "扩展列");
		ext.put("AD_Y_S", "扩展列");
		ext.put("PD_M_S", "扩展列");
		ext.put("PD_Y_S", "扩展列");
		ext.put("IPC_B_S", "扩展列");
		ext.put("IPC_DL_S", "扩展列");
		ext.put("IPC_XL_S", "扩展列");
		ext.put("IPC_DZ_S", "扩展列");
		ext.put("PFN_COUNT", "扩展列");
		ext.put("CDN_COUNT", "扩展列");
		ext.put("LSE", "扩展列");
		ext.put("LS", "扩展列");
		ext.put("LSE", "扩展列");
		ext.put("LE", "扩展列");
		ext.put("LSD", "扩展列");
		ext.put("LSD_Y_S", "扩展列");
		ext.put("LSI", "扩展列");
		ext.put("LSND", "扩展列");
		ext.put("LSN", "扩展列");
		ext.put("LSNE", "扩展列");
		ext.put("LSNI", "扩展列");
		ext.put("docNo", "扩展列");
		ext.put("UID", "扩展列");
		ext.put("CNLX", "扩展列");
		ext.put("T_NAME", "扩展列");
	}
}
