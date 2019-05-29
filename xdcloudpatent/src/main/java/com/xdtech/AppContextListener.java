package com.xdtech;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.xdtech.patent.action.ParamContext;
import com.xdtech.patent.conf.AppContextUtils;
import com.xdtech.patent.ipc.IPCContext;
import com.xdtech.patent.service.ParamService;

/**
 * web应用监听器
 * 
 * @author Chang Fei
 */
public class AppContextListener implements ServletContextListener {
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	public void contextInitialized(ServletContextEvent arg0) {
		IPCContext.init();
		AppContextUtils.setServletContext(arg0.getServletContext());
		ParamService paramService = AppContextUtils.getBean("paramService");
		paramService.initSystemParam();
		//ParamContext.init();

	}
	
	
	
}
