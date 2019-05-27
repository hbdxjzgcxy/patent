package com.xdtech;

import java.io.File;

import com.xdtech.patent.conf.AppConf;

public class SysProperties {

	public static final String CURRENT_USER = "user";
	public static final String ROLE_ADMIN = "admin";
	public static final String ROLE_COMPANY = "company";
	public static final String ROLE_COMPANY_CHILD = "company_child";
	private static final String DATA_DIR = System.getProperty("user.home") + "/xdtech-patent";

	/**
	 * 用户数据目录
	 */
	public static String USER_DATA_DIR = AppConf.get().get("user.data.dir", DATA_DIR);

	static {
		if (USER_DATA_DIR.startsWith("~/")) {
			USER_DATA_DIR = System.getProperty("user.home") + File.separator+ USER_DATA_DIR.substring(2);
		}
		System.out.println("您的数据存放目录：");
		System.out.println(new File(USER_DATA_DIR).getAbsolutePath());
	}

}
