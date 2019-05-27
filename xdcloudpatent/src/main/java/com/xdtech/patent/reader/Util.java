package com.xdtech.patent.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.dom4j.DocumentException;

import com.xdtech.patent.action.SearchBean;

/**
 * 工具类 TODO://有待重构
 * 
 * @author changfei
 *
 */
public class Util {
	static Set<String> getHeadStr(String[] ipc, int start, int end) {
		Set<String> b = new LinkedHashSet<String>();
		for (String ipcStr : ipc) {
			ipcStr = ipcStr.replace(" ", "").trim();
			b.add(StringUtils.substring(ipcStr, start, end));
		}
		return b;
	}

	/**
	 * 多值拆分
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	static String[] splitAndTrim(String key, String value, Map<String, ImportCfg> cfgs) {
		if (value != null) {
			String split = null;
			ImportCfg importCfg = cfgs.get(key);
			if (importCfg == null) {
				throw new NullPointerException("字段" + key + "的配置未找到");
			}
			char[] splitChars = importCfg.getSplitChars();
			if (splitChars != null && splitChars.length > 0) {
				for (char c : splitChars) {
					if (value.indexOf(c) > -1) {
						split = c + "";
						break;
					}
				}
			}
			if ("|".equals(split)) {
				split = "\\|";
			}
			if (null == split) {
				split = ";";
				PatentReader.log.warn("使用默认分隔符[;]分隔IPC=" + value);
			}
			String[] array = value.split(split);
			for (int i = 0; i < array.length; i++) {
				array[i] = array[i].trim();
			}
			return array;
		}
		return new String[] {};
	}

	/**
	 * 连接集合
	 * 
	 * @param vals
	 * @param str
	 * @return
	 */
	static String joinSet(Set<String> vals, String str) {
		if (vals != null && vals.size() > 0) {
			return StringUtils.join(vals, str);
		}
		return "";
	}

	/**
	 * 连接数组
	 * 
	 * @param vals
	 * @param str
	 * @return
	 */
	static String jionArray(String[] vals, String str) {
		if (vals != null && vals.length > 0) {
			return StringUtils.join(vals, str);
		}
		return "";
	}

	/**
	 * 遍历附图
	 * 
	 * @param versionIs2003
	 * @param sheet
	 * @return
	 */
	static Map<String, Object> getImgs(boolean versionIs2003, Sheet sheet) {
		Map<String, Object> imgRows = new HashMap<>();
		if (versionIs2003) {
			HSSFSheet sheet2003 = (HSSFSheet) sheet;
			HSSFPatriarch drawingPatriarch = sheet2003.getDrawingPatriarch();
			if (drawingPatriarch != null) {
				List<HSSFShape> children = drawingPatriarch.getChildren();
				children.forEach(e -> {
					HSSFClientAnchor anchro = (HSSFClientAnchor) e.getAnchor();
					imgRows.put(anchro.getRow1() + "", e);
				});
			}
		} else {
			XSSFSheet sheet2007 = (XSSFSheet) sheet;
			XSSFDrawing drawingPatriarch = sheet2007.getDrawingPatriarch();
			if (drawingPatriarch != null) {
				drawingPatriarch.getShapes().forEach(e -> {
					XSSFClientAnchor anchor = (XSSFClientAnchor) e.getAnchor();
					imgRows.put(anchor.getRow1() + "", e);
				});
			}
		}
		return imgRows;
	}

	static void saveAttachImgToResource(String path, byte[] imgBytes, String pn, String extName) {
		File root = new File(new File(path), SearchBean.pn2Path(pn));
		if (!root.exists()) {
			root.mkdirs();
		}
		File imgFile = new File(root, pn + "." + extName);
		try (FileOutputStream fos = new FileOutputStream(imgFile)) {
			fos.write(imgBytes, 0, imgBytes.length);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理多值字段
	 * 
	 * @param field
	 * @param cellVal
	 * @return
	 */
	static String proc(String field, String cellVal, Map<String, ImportCfg> cfgs) {
		if (StringUtils.isNotEmpty(cellVal)) {
			return jionArray(splitAndTrim(field, cellVal, cfgs), PatentReader.JOIN_CHAR);
		}
		return "";
	}

	/**
	 * // B61D002500 国外专利号 -->B61D25/00
	 * 
	 * @param ipc
	 * @return
	 */
	static String normalizeIPCAndCPC(String ipc, Map<String, ImportCfg> cfgs) {
		if (StringUtils.isNotEmpty(ipc)) {
			String[] ipcs = splitAndTrim("IPC", ipc, cfgs);
			for (int i = 0; i < ipcs.length; i++) {
				if (ipcs[i].indexOf("/") == -1) {
					// B61D002500 国外专利号 -->B61D 25/00
					String main = StringUtils.substring(ipcs[i], 0, 4); // IPC主部
					String remain = StringUtils.substring(ipcs[i], 4);
					String dz = ""; // 大组
					String xz = ""; // 小组
					if (!remain.contains("/")) {
						if (remain.length() > 2 && remain.charAt(2) == '0') {
							dz = StringUtils.substring(remain, 3, 4);
						} else {
							dz = StringUtils.substring(remain, 2, 4);
						}
						xz = "/" + StringUtils.substring(remain, 4);
						ipcs[i] = main + dz + xz;
					}
				}
			}
			return jionArray(ipcs, PatentReader.JOIN_CHAR);
		}
		return "";
	}

	static String count(String mulitValue) {
		if (StringUtils.isNotEmpty(mulitValue)) {
			return Integer.toString(mulitValue.split(PatentReader.JOIN_CHAR).length);
		}
		return "0";
	}

	/**
	 * 创建一个以Web root 为根的一个资源目录
	 * 
	 * @param path
	 * @return
	 */
	public static String createWebPath(String path) {
		File file = new File(getWebPath(), path);
		return file.getPath();
	}

	public static String getWebPath() {
		String classesPath = PatentReader.class.getResource("/").getFile();
		try {
			classesPath = URLDecoder.decode(classesPath, "UTF-8");// classPath会对路径中的空格编码,需要解码
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		File file = new File(classesPath);
		File webroot = file.getParentFile().getParentFile(); // 定位到webroot
		return webroot.getPath();
	}

	public static void main(String[] args) {
		try {
			System.out.println(normalizeIPCAndCPC("C12N000112 | C12P000502", ImportCfg.getDefault()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
