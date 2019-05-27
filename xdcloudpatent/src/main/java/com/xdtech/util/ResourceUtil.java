package com.xdtech.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.xdtech.SysProperties;

/**
 * 资源工具类
 * @author changfei
 *
 */
public class ResourceUtil {

	private static final String BASE_DIR = SysProperties.USER_DATA_DIR;

	private static final String RESOURCE_NAME = "resources";

	public static final String RESOURCE_PATENT_IMG = "patent-img";

	public static final String RESOURCE_PATENT_PDF = "patent-pdf";

	public static final String RESOURCE_HEDER_IMG = "image";

	public static final String RESOURCE_PATENT_TEMPLATE = "patent-import-template";

	/**
	 * 导入专利附图
	 * @param identity 用户标示
	 * @param an
	 * @param extName 附图扩展名
	 * @param img 附图
	 */
	public static void importPatentImg(String identity, String pn, String extName, byte[] imgBytes) {
		String imgResPath = getUserResourceDir(identity, RESOURCE_PATENT_IMG, true);
		String npath = resolvePN2Path(pn);
		File root = new File(new File(imgResPath), npath);
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
	 * 获取用户的专利附图
	 * @param uid 用户Id
	 * @param an
	 * @return
	 */
	public static String getPatentImg(String identity, String pn) {
		checkPn(pn);
		String imgResPath = getUserResourceDir(identity, RESOURCE_PATENT_IMG, false);
		String npath = resolvePN2Path(pn);
		File resDir = new File(imgResPath, npath);
		return findImg(resDir, pn);
	}

	private static void checkPn(String pn) {
		if (pn == null) {
			throw new NullPointerException("pn is null");
		}
	}

	/**
	 * 获取用户的专利附图
	 * @param uid 用户Id
	 * @param an
	 * @return
	 */
	public static String getPatentPdf(String identity, String pn) {
		checkPn(pn);
		String imgResPath = getUserResourceDir(identity, RESOURCE_PATENT_PDF, false);
		String npath = resolvePN2Path(pn);
		File resDir = new File(imgResPath, npath);
		return new File(resDir, pn.toUpperCase() + ".PDF").getAbsolutePath();
	}

	private static String findImg(File resDir, String pn) {
		if (resDir != null && resDir.exists()) {
			for (File file : resDir.listFiles()) {
				String name = FilenameUtils.getBaseName(file.getName());
				if (name.toUpperCase().equals(pn.toUpperCase()) && file.isFile()) {
					return file.getAbsolutePath();
				}
			}
		}
		return null;
	}

	/**
	 * 专利号转换成资源存储路径。
	 * <p>
	 * CN20136542==>CN/2013/6542/
	 * @param pn 专利PN号
	 * 
	 * @return 转换后的相对位置
	 */
	public static String resolvePN2Path(String pn) {
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
	/**
	 * 专利号转为下划线分割的专利号名称
	 * pn=US20030231277A1 --->US-20030231277-A1
	 * @param pn
	 * @return
	 */
	public static String resolvePn2Underline(String pn){
		if(pn.contains("/")){
			pn = pn.split("/")[1];
		}
		StringBuilder sb = new StringBuilder();
		sb.append(pn.substring(0,2));
		sb.append("-");
		String lastStr = pn.substring(pn.length()-1, pn.length());
		String reg="^\\d+$";
		boolean result = lastStr.matches(reg);
		if(result){
			sb.append(pn.substring(2,pn.length()-2));
			sb.append("-");
			sb.append(pn.substring(pn.length()-2));
		}else{
			sb.append(pn.substring(2,pn.length()-1));
			sb.append("-");
			sb.append(pn.substring(pn.length()-1));
		}
		return sb.toString();
	}
	
	/**
	 * 获取用户的资源目录
	 * @param identity
	 * @param dir
	 * @return
	 */
	public static String getUserResourceDir(String identity, String dir, boolean create) {
		File path = new File(BASE_DIR, identity);
		path = new File(path, RESOURCE_NAME);
		path = new File(path, dir);
		if (!path.exists() && create) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
	}

	/**
	 * 获取用户导入模板的存放目录
	 * @param identity
	 * @return
	 */
	public static String getUserTemplateDir(String identity) {
		return getUserResourceDir(identity, RESOURCE_PATENT_TEMPLATE, true);
	}

	/**
	 * 获取用户导入模板的存放目录
	 * @param identity
	 * @return
	 */
	public static String getUserImageDir(String identity) {
		return getUserResourceDir(identity, RESOURCE_HEDER_IMG, true);
	}

	public static String getUserImage(String identity) {
		String userResourceDir = getUserResourceDir(identity, RESOURCE_HEDER_IMG, true);
		File photo = new File(userResourceDir, identity + ".png");
		if (!photo.exists())
			photo = new File(userResourceDir, identity + ".jpg");
		if (photo.exists())
			return photo.getAbsolutePath();
		return null;
	}
}
