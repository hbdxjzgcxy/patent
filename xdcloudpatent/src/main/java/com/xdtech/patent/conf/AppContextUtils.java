package com.xdtech.patent.conf;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Chang Fei
 */
@SuppressWarnings("unchecked")
public class AppContextUtils {

	private static Set<String> disabledUserSet = new HashSet<String>();

	private static Logger LOGGER = Logger.getLogger(AppContextUtils.class);
	/** ServletContext */
	private static ServletContext appContext;

	/** sprig WebApplicationContext 容器 */
	private static WebApplicationContext springAppContext;

	private static String realPath = null;

	/** 设置 ServletConttext */
	public static void setServletContext(ServletContext context) {
		appContext = context;
		realPath = context.getRealPath("");
		LOGGER.info("项目部署路径：" + realPath);
	}

	/** 获取servletContext */
	public static ServletContext getServletContext() {
		return appContext;
	}

	/** 获取Spring WebApplicationContext 容器 */
	public static WebApplicationContext getSpringAppContext() {
		if (springAppContext == null) {
			springAppContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		}
		return springAppContext;
	}

	public static String getRealPath() {
		return realPath;
	}

	public static void setRealPath(String realPath) {
		AppContextUtils.realPath = realPath;
	}

	public static String getContextPath() {
		return appContext.getContextPath();
	}

	/**
	 * @param <T>
	 *            返回值
	 * @param idOrBeanName
	 *            spring container中bean的id或者bean的name.
	 * @return
	 */
	public static <T> T getBean(String idOrBeanName) {
		if (getSpringAppContext().containsBean(idOrBeanName)) {
			return (T) getSpringAppContext().getBean(idOrBeanName);
		} else {
			return null;
		}
	}

	public static void addDisabledUser(int id) {
		disabledUserSet.add(id + "");
	}

	public static void removeDisabledUser(int id) {
		disabledUserSet.remove(id + "");
	}

	public static boolean isDisabledUser(int id) {
		return disabledUserSet.contains(id+"");
	}

}
