package com.xdtech.patent.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.DocumentException;
import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.xdtech.SysProperties;
import com.xdtech.patent.entity.User;
import com.xdtech.search.client.ws.StringArray;
import com.xdtech.util.BdbDedupl;
import com.xdtech.util.Dedupl;
import com.xdtech.util.ResourceUtil;

public class PatentReader {

	public static String imgPath;
	
	public static String topicStatu;

	/** 专利PDF路径 */
	public static String pdfPath;
	/** 多值字段连接符 */
	final static String JOIN_CHAR = ";";

	final static String BASE_DIR = SysProperties.USER_DATA_DIR;

	final static Logger log = org.slf4j.LoggerFactory.getLogger(PatentReader.class);

	static Dedupl tnameDB = null;

	static Dedupl pathDB = null;

	Map<String, ImportCfg> cfgs = null;

	private List<String> head = new ArrayList<>();

	private List<StringArray> datas = new ArrayList<>();

	private static List<String> BASE_KEY_LIST = Lists.newArrayList("AB", "AN", "PN", "AD", "AU", "AGT", "AGC", "PA",
			"ADDR", "CLM", "PD", "PR", "PRC", "PRD", "PRN", "TI", "IPC", "LSE", "CPC", "PFN", "CDN", "CTN",
			"EXT_DISPLAY_1", "EXT_DISPLAY_2", "EXT_DISPLAY_3", "LS", "PC", "FT");
	private static List<String> AD_EXT = Lists.newArrayList("AD_Y_S", "AD_M_S");
	private static List<String> PRD_EXT = Lists.newArrayList("PRD_Y_S", "PRD_M_S");
	private static List<String> PD_EXT = Lists.newArrayList("PD_Y_S", "PD_M_S");
	private static List<String> PN_EXT = Lists.newArrayList("AC", "CNLX");
	private static List<String> LSE_EXT = Lists.newArrayList(/* "LS", */"LE", "LSD", "LSN", "LSD_Y_S", "LSI", "LSND",
			"LSNE", "LSNI");
	private static List<String> IPC_EXT = Lists.newArrayList("IPC_B_S", "IPC_DL_S", "IPC_XL_S", "IPC_DZ_S", "MIPC",
			"MIPC_B_S", "MIPC_DL_S", "MIPC_DZ_S", "MIPC_XL_S");
	private static List<String> PA_EXT = Lists.newArrayList("PA_S");
	private static List<String> AU_EXT = Lists.newArrayList("AU_S");
	private static List<String> CPC_EXT = Lists.newArrayList("CPC_B_S", "CPC_DL_S", "CPC_XL_S", "CPC_DZ_S");
	private static List<String> CDN_EXT = Lists.newArrayList("CDN_COUNT");
	private static List<String> PFN_EXT = Lists.newArrayList("PFN_COUNT");
	private static List<String> CTN_EXT = Lists.newArrayList("CTN_COUNT");

	public PatentReader(String uid, String templateName) {
		try {
			if (StringUtils.isEmpty(templateName))
				cfgs = ImportCfg.getDefault();
			else {
				String templeteDir = ResourceUtil.getUserTemplateDir(uid);
				File tpl = new File(templeteDir, templateName);
				cfgs = ImportCfg.get(tpl.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public PatentReader read(File file, List<String> customerFields, User user, String topicName)
			throws FileNotFoundException, IOException, InvalidFormatException, DocumentException {
		String fileName = file.getName();
		String path = topicName + "#" + FilenameUtils.getBaseName(fileName).replace("-", "#");
		String extName = FilenameUtils.getExtension(fileName);
		boolean versionIs2003 = false;
		Workbook book = null;
		try {
			if (extName.toLowerCase().equals("xls")) {
				versionIs2003 = true;
				book = new HSSFWorkbook(new FileInputStream(file));
			} else {
				book = new XSSFWorkbook(file);
			}
			Sheet sheet = book.getSheetAt(0);
			Map<String, Object> imgRows = Util.getImgs(versionIs2003, sheet); // 遍历附图所在的行号
			Row titleRow = sheet.getRow(0);

			Map<String, Integer> titleMap = new HashMap<>();
			for (int i = 0; i < titleRow.getLastCellNum(); i++) {
				String title = titleRow.getCell(i).getStringCellValue(); // excle标题
				titleMap.put(title, i);
			}

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				StringBuilder docNoStrb = new StringBuilder().append(user.getId()).append("!");
				StringArray array = new StringArray();
				List<String> AD_Ext = Lists.newArrayList("", "");
				List<String> PRD_Ext = Lists.newArrayList("", "");
				List<String> PD_Ext = Lists.newArrayList("", "");
				List<String> PN_Ext = Lists.newArrayList("", "");
				List<String> IPC_Ext = Lists.newArrayList("", "", "", "");
				List<String> LSE_Ext = Lists.newArrayList(/* "", */"", "", "", "", "", "", "", "");
				List<String> PA_Ext = Lists.newArrayList("");
				List<String> AU_Ext = Lists.newArrayList("");
				List<String> CPC_Ext = Lists.newArrayList("", "", "", "");
				String CDN_Ext = "0";
				String PFN_Ext = "0";
				String CTN_Ext = "0";
				Row dataRow = sheet.getRow(i);
				for (String key : BASE_KEY_LIST) {
					String cellVal = "";
					ImportCfg cfg = cfgs.get(key);
					if (cfg == null && !customerFields.isEmpty()) {
						cellVal = getCustCellVal(key, customerFields, titleMap, dataRow);
					} else {
						cellVal = getCellValue(titleMap, dataRow, cfg);
					}
					if (!requireCheck(key, cellVal)) {
						array = null;
						break;
					} else {
						if ("PRD".equals(key)) {
							if (StringUtils.isEmpty(cellVal)) {
								cellVal = "00000000"; // 优先权日空值用00000000替换
							} else {
								cellVal = cellVal.replace("-", "");
							}
							PRD_Ext = PRDExtProc.getPRD_Ext(cellVal);
						} else if ("AD".equals(key)) {
							if (StringUtils.isEmpty(cellVal)) {
								cellVal = "00000000"; // 申请日空值用00000000替换
							} else {
								cellVal = cellVal.replace("-", "");
							}
							AD_Ext = ADExtProc.getAD_Ext(cellVal);
							docNoStrb.append("_").append(cellVal);
						} else if ("PD".equals(key)) {
							if (StringUtils.isEmpty(cellVal)) {
								cellVal = "00000000"; // 公开日空值用00000000替换
							} else {
								cellVal = cellVal.replace("-", "");
							}
							PD_Ext = PDExtProc.getPD_Ext(cellVal);
						} else if ("PN".equals(key)) {
							PN_Ext = PNExtProc.getPN_Ext(cellVal);
							docNoStrb.append("_").append(cellVal);
							Object img = imgRows.get(i + "");
							if (img != null) {
								Picture pic = (Picture) img;
								byte[] imgBytes = pic.getPictureData().getData();
								String picExtName = pic.getPictureData().suggestFileExtension();
								ResourceUtil.importPatentImg(user.getUsername(), cellVal, picExtName, imgBytes);
							}
						} else if ("LSE".equals(key)) {
							LSE_Ext = LSEExtProc.getLSE_Ext(cellVal);
							cellVal = Util.proc(key, cellVal, cfgs);
						} else if ("IPC".equals(key)) {
							cellVal = Util.normalizeIPCAndCPC(cellVal, cfgs);
							IPC_Ext = IPCExtProc.getIPC_Ext(cellVal, cfgs);
						} else if ("AN".equals(key)) {
							docNoStrb.append(cellVal);
						} else if ("AU".equals(key)) {
							AU_Ext = AUExtProc.getAU_Ext(cellVal, cfgs);
						} else if ("PA".equals(key)) {
							PA_Ext = PAExtProc.getPA_Ext(cellVal, cfgs);
						} else if ("PFN".equals(key)) {
							cellVal = Util.proc(key, cellVal, cfgs);
							PFN_Ext = Util.count(cellVal);
						} else if ("CDN".equals(key)) {
							cellVal = Util.proc(key, cellVal, cfgs);
							CDN_Ext = Util.count(cellVal);
						} else if ("CTN".equals(key)) {
							cellVal = Util.proc(key, cellVal, cfgs);
							CTN_Ext = Util.count(cellVal);
						} else if ("CPC".equals(key)) {
							cellVal = Util.normalizeIPCAndCPC(cellVal, cfgs);
							CPC_Ext = CPCExtProc.getCPC_Ext(cellVal, cfgs);
						} else if ("PC".equals(key)) {
							if (StringUtils.isNotEmpty(cellVal)) {
								int idx = cellVal.indexOf(";");
								if (idx > -1) {
									cellVal = cellVal.substring(0, idx);
								}
							}
						} else if ("PRC".equals(key)) {
							if (StringUtils.isNotEmpty(cellVal)) {
								int idx = cellVal.indexOf(";");
								if (idx > -1) {
									cellVal = cellVal.substring(0, idx);
								}
							}
						}
					}
					array.getItem().add(cellVal);
				}
				if (array == null) {
					log.error(file.getName() + "第" + i + "行数据错误:AN/PN是必须字段不能为空");
				} else {
					array.getItem().addAll(AD_Ext);
					array.getItem().addAll(PRD_Ext);
					array.getItem().addAll(PD_Ext);
					array.getItem().addAll(PN_Ext);
					array.getItem().addAll(LSE_Ext);
					array.getItem().addAll(IPC_Ext);
					array.getItem().addAll(AU_Ext);
					array.getItem().addAll(PA_Ext);
					array.getItem().addAll(CPC_Ext);
					array.getItem().add(PFN_Ext);
					array.getItem().add(CDN_Ext);
					array.getItem().add(CTN_Ext);
					array.getItem().add(getTopicName(docNoStrb.toString(), topicName));
					array.getItem().add(docNoStrb.toString());
					array.getItem().add(Integer.toString(user.getId()));
					array.getItem().add(getPath(docNoStrb.toString(), path));
					this.datas.add(array);
				}
			}
		} finally {
			book.close();
		}
		this.head.addAll(BASE_KEY_LIST);
		this.head.addAll(AD_EXT);
		this.head.addAll(PRD_EXT);
		this.head.addAll(PD_EXT);
		this.head.addAll(PN_EXT);
		this.head.addAll(LSE_EXT);
		this.head.addAll(IPC_EXT);
		this.head.addAll(AU_EXT);
		this.head.addAll(PA_EXT);
		this.head.addAll(CPC_EXT);
		this.head.addAll(PFN_EXT);
		this.head.addAll(CDN_EXT);
		this.head.addAll(CTN_EXT);
		this.head.add("T_NAME");
		this.head.add("docNo");
		this.head.add("UID");
		this.head.add("path");
		tnameDB.flush();
		pathDB.flush();
		return this;

	}

	private static String getCustCellVal(String key, List<String> customerFields, Map<String, Integer> titleMap,
			Row dataRow) {
		String cellVal = "";
		if (customerFields != null && !customerFields.isEmpty()) {
			Integer cellnum = null;
			String cellTitle = "";
			if ("EXT_DISPLAY_1".equals(key) && customerFields.size() >= 1) {
				String key_val[] = customerFields.get(0).split("!");
				cellTitle = key_val[0];
				cellnum = titleMap.get(key_val[1]);
			} else if ("EXT_DISPLAY_2".equals(key) && customerFields.size() >= 2) {
				String key_val[] = customerFields.get(1).split("!");
				cellTitle = key_val[0];
				cellnum = titleMap.get(key_val[1]);
			} else if ("EXT_DISPLAY_3".equals(key) && customerFields.size() > 2) {
				String key_val[] = customerFields.get(2).split("!");
				cellTitle = key_val[0];
				cellnum = titleMap.get(key_val[1]);
			}
			if (cellnum != null) {
				Cell cell = dataRow.getCell(cellnum);
				if (cell != null) {
					cellVal = getCellValue(cell);
				}
			}
			cellVal = cellTitle + "=" + cellVal;
		}
		return cellVal;
	}

	private static String getCellValue(Cell cell) {
		String valueString = "";
		if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
			if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				valueString = String.valueOf(cell.getBooleanCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				valueString = cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				if (!HSSFDateUtil.isCellDateFormatted(cell)) {
					valueString = String.valueOf(cell.getNumericCellValue());
					if (valueString.endsWith(".0")) {
						valueString = valueString.replace(".0", "");
					}
				} else {
					Date date = cell.getDateCellValue();
					if (date != null) {
						long time = date.getTime();
						if (time >= -2209104000000L && time <= -2209017601000L) {
							valueString = new SimpleDateFormat("HH:mm:ss").format(date);
						} else {
							valueString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
						}
					}
				}
			}
		}
		return valueString;
	}

	/**
	 * 根据ImportCfg规则找到Excel单元中和索引字段匹配的数据。
	 * 
	 * @param titleMap
	 * @param dataRow
	 * @param cfg
	 * @return
	 */
	private static String getCellValue(Map<String, Integer> titleMap, Row dataRow, ImportCfg cfg) {
		String cellval = ""; // 默认
		if (cfg != null) {
			Integer cellIndex = null;
			for (String tname : cfg.getColumn()) {
				cellIndex = titleMap.get(tname);
				if (cellIndex != null) {
					Cell cell = dataRow.getCell(cellIndex);
					if (cell != null) {
						cellval = cell.getStringCellValue();
						if (StringUtils.isNotEmpty(cellval)) {
							break;
						}
					}
				}
			}

		}
		return cellval;
	}

	/**
	 * 必要字段检查
	 * 
	 * @param key
	 * @param val
	 * @return
	 */
	private static boolean requireCheck(String key, String val) {
		if ("AN".equals(key) || "PN".equals(key)) {
			return StringUtils.isNotEmpty(val);
		}
		return true;
	}

	private PatentReader() {

	}

	public final static Map<String, String> header = new HashMap<>();

	public List<String> getHead() {
		return head;
	}

	public List<StringArray> getData() {
		return datas;
	}

	/**
	 * 专题名字去重
	 * 
	 * @param docNo
	 * @param topic
	 * @return
	 */
	private static String getTopicName(String docNo, String topic) {
		String returnTopic = topic;
		String topicNames = tnameDB.getRecord(docNo);
		if (topicNames == null || topicNames.isEmpty()) {
			tnameDB.saveRecord(docNo, topic);
		} else {
			boolean exists = false;
			for (String tname : topicNames.split(";")) {
				if ((exists = tname.equals(topic))) {
					break;
				}
			}
			if (!exists) {
				returnTopic = topicNames + ";" + topic;
				tnameDB.saveRecord(docNo, returnTopic);
			}else{
				//专题名称重复
				returnTopic = topicNames;
			}
		}
		//赋值给专题状态
		topicStatu = returnTopic;
		return returnTopic;
	}

	private static String getPath(String docNo, String path) {
		String realPath = path;
		String topicNames = pathDB.getRecord(docNo);
		if (topicNames == null || topicNames.isEmpty()) {
			pathDB.saveRecord(docNo, path);
		} else {
			boolean exists = false;
			for (String tname : topicNames.split(";")) {
				if ((exists = tname.equals(path))) {
					break;
				}
			}
			if (!exists) {
				realPath = topicNames + ";" + path;
				pathDB.saveRecord(docNo, realPath);
			}else{
				//专题名称重复
				realPath = topicNames;
			}
		}
		return realPath;
	}

	static {
		init();
	}

	private static void init() {
		// loadTemplate(templateName);
		tnameDB = BdbDedupl.get(new File(BASE_DIR, "tname-db").getPath(), true);
		pathDB = BdbDedupl.get(new File(BASE_DIR, "path-db").getPath(), true);

	}
	/*
	 * private static void loadTemplate(String templateName) throws IOException,
	 * DocumentException { if (StringUtils.isEmpty(templateName)) cfgs =
	 * ImportCfg.getDefault(); else { cfgs = ImportCfg.get(templateName); } }
	 */

}
