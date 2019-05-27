package com.xdtech.patent.action;

import org.apache.commons.lang.StringUtils;

import com.xdtech.patent.reader.PatentReader;

public class SearchBean {
	private String core;
	private String id;
	private String docNo;
	private String dataSource;
	private String summary;
	private float score;

	private String AB;
	private String AC;
	private String AD;
	private String ADDR;
	private String AN;
	private String AU;
	private String PA;
	private String PD;
	private String PN;
	private String PR;
	private String TI;
	private String IPC;
	private String PC;
	private String AGC;
	private String AGT;
	private String LS;
	private String LSN;
	private String LSE;
	private String PFN;
	private String CDN;
	private String CTN;
	private String CPC;
	private String FT;

	//扩展字段
	private String EXT_DISPLAY_1;
	private String EXT_DISPLAY_2;
	private String EXT_DISPLAY_3;

	private String CLM;

	public String getCore() {
		return core;
	}

	public void setCore(String core) {
		this.core = core;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDocNo() {
		return docNo;
	}

	public String getDocId() {
		return docNo.substring(docNo.indexOf("!") + 1);
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getPD() {
		return PD;
	}

	public void setPD(String pD) {
		PD = pD;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getAB() {
		return AB;
	}

	public void setAB(String aB) {
		AB = aB;
	}

	public String getAC() {
		return AC;
	}

	public void setAC(String aC) {
		AC = aC;
	}

	public String getAD() {
		return AD;
	}

	public void setAD(String aD) {
		AD = aD;
	}

	public String getADDR() {
		return ADDR;
	}

	public void setADDR(String aDDR) {
		ADDR = aDDR;
	}

	public String getAN() {
		return AN;
	}

	public void setAN(String aN) {
		AN = aN;
	}

	public String getAU() {
		return AU;
	}


	public void setAU(String aU) {
		AU = aU;
	}
	public String getFT() {
		return FT;
	}
	
	public void setFT(String fT) {
		FT = fT;
	}

	public String getPA() {
		return PA;
	}

	public void setPA(String pA) {
		PA = pA;
	}

	public String getPN() {
		return PN;
	}

	public void setPN(String pN) {
		PN = pN;
	}
	
	

	public String getPFN() {
		return PFN;
	}
	
	public String[] getPFNS() {
		if(StringUtils.isNotEmpty(PFN)){
			return PFN.split(";");
		}
		return new String[]{};
	}


	public void setPFN(String pFN) {
		PFN = pFN;
	}


	public String getCDN() {
		return CDN;
	}
	
	public String[] getCDNS() {
		if(StringUtils.isNotEmpty(CDN)){
			return CDN.split(";");
		}
		return new String[]{};
	}

	public void setCDN(String cDN) {
		CDN = cDN;
	}

	public String getCTN() {
		return CTN;
	}
	
	public String[] getCTNS() {
		if(StringUtils.isNotEmpty(CTN)){
			return CTN.split(";");
		}
		return new String[]{};
	}

	public void setCTN(String cTN) {
		CTN = cTN;
	}

	public String getTI() {
		if (StringUtils.isEmpty(TI)) {
			return "无标题";
		}
		return TI;
	}

	public void setTI(String tI) {
		TI = tI;
	}

	public String getIPC() {
		return IPC;
	}

	public void setIPC(String iPC) {
		IPC = iPC;
	}

	public String getPC() {
		return PC;
	}

	public void setPC(String pC) {
		PC = pC;
	}

	public String getAGC() {
		return AGC;
	}

	public void setAGC(String aGC) {
		AGC = aGC;
	}

	public String getAGT() {
		return AGT;
	}

	public void setAGT(String aGT) {
		AGT = aGT;
	}

	public String getPR() {
		return PR;
	}

	public void setPR(String pR) {
		PR = pR;
	}

	public String getLS() {
		return LS;
	}

	public void setLS(String lS) {
		LS = lS;
	}

	public String getCLM() {
		return CLM;
	}

	public void setCLM(String cLM) {
		CLM = cLM;
	}

	public String getLSN() {
		return LSN;
	}

	public void setLSN(String lSN) {
		LSN = lSN;
	}

	public String getLSE() {
		return LSE;
	}

	public void setLSE(String lSE) {
		LSE = lSE;
	}
	

	public String getCPC() {
		return CPC;
	}

	public void setCPC(String cPC) {
		CPC = cPC;
	}

	public String getEXT_DISPLAY_1() {
		return EXT_DISPLAY_1;
	}

	public void setEXT_DISPLAY_1(String eXT_DISPLAY_1) {
		EXT_DISPLAY_1 = eXT_DISPLAY_1;
	}

	public String getEXT_DISPLAY_2() {
		return EXT_DISPLAY_2;
	}

	public void setEXT_DISPLAY_2(String eXT_DISPLAY_2) {
		EXT_DISPLAY_2 = eXT_DISPLAY_2;
	}

	public String getEXT_DISPLAY_3() {
		return EXT_DISPLAY_3;
	}

	public void setEXT_DISPLAY_3(String eXT_DISPLAY_3) {
		EXT_DISPLAY_3 = eXT_DISPLAY_3;
	}

	public String getExtKey1() {
		if (StringUtils.isNotEmpty(EXT_DISPLAY_1) && EXT_DISPLAY_1.length() > 1) {
			return EXT_DISPLAY_1.split("=")[0];
		}
		return "";
	}

	public String getExtValue1() {
		if (StringUtils.isNotEmpty(EXT_DISPLAY_1) && EXT_DISPLAY_1.length() > 1) {
			String[] split = EXT_DISPLAY_1.split("=");
			if(split.length>1){
				return split[1];
			}else{
				return "";
			}
		}
		return "";
	}

	public String getExtKey2() {
		if (StringUtils.isNotEmpty(EXT_DISPLAY_2) && EXT_DISPLAY_2.length() > 1) {
			return EXT_DISPLAY_2.split("=")[0];
		}
		return "";
	}

	public String getExtValue2() {
		if (StringUtils.isNotEmpty(EXT_DISPLAY_2) && EXT_DISPLAY_2.length() > 1) {
			String[] split = EXT_DISPLAY_2.split("=");
			if(split.length>1){
				return split[1];
			}else{
				return "";
			}
		}
		return "";
	}

	public String getExtKey3() {
		if (StringUtils.isNotEmpty(EXT_DISPLAY_3) && EXT_DISPLAY_3.length() > 1) {
			return EXT_DISPLAY_3.split("=")[0];
		}
		return "";
	}

	public String getExtValue3() {
		if (StringUtils.isNotEmpty(EXT_DISPLAY_3) && EXT_DISPLAY_3.length() > 1) {
			String[] split = EXT_DISPLAY_3.split("=");
			if(split.length>1){
				return split[1];
			}else{
				return "";
			}
		}
		return "";
	}

	

	public static void main(String[] args) {
		System.out.println(pn2Path("CN20136542"));
	}

	/**
	 * 专利号转换成资源存储路径。
	 * <p>
	 * CN20136542==>CN/2013/6542/
	 * @param pn 专利PN号
	 * 
	 * @return 转换后的相对位置
	 */
	public static String pn2Path(String pn) {
		/*
		 * 前2位国别分隔开
		 */
		StringBuilder path = new StringBuilder();
		if (!StringUtils.isEmpty(pn)) {
			String PN = pn;
			/*
			 * 首先去掉公开号的校验位
			 */
			int dotIdx = pn.indexOf(".");
			if (dotIdx > -1) {
				PN = pn.substring(0, dotIdx);
			}

			if (PN.length() > 2) {
				path.append(PN.substring(0, 2)).append("/");
			}
			/*
			 * 其余部分每4位一份
			 */
			int len = 2;
			while (len + 4 < PN.length()) {
				path.append(PN.substring(len, len + 4)).append("/");
				len += 4;
			}
			if (len != PN.length()) {
				try {
					path.append(PN.substring(len, PN.length())).append("/");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return path.toString();
	}
}