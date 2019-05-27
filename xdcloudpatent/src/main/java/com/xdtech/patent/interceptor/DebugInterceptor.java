package com.xdtech.patent.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.xdtech.patent.conf.AppConf;

/**
 * 调试拦截器
 * 
 * @author Chang Fei
 */
public class DebugInterceptor extends HandlerInterceptorAdapter {
	private static Logger logger = LoggerFactory.getLogger("DebugInterceptor");
	private static final boolean debugModel = AppConf.get().getBoolean("system.debug", false);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (debugModel) {
			debugInfo("");
			debugInfo("path: http://" + request.getLocalAddr() + ":" + request.getLocalPort() + request.getRequestURI());
			debugInfo("method:"+request.getMethod());
			if("GET".equals(request.getMethod())){
				debugInfo("parameters:" + request.getQueryString());
			}else if("POST".equals(request.getMethod())){
				Enumeration<String> names =request.getParameterNames();
				StringBuilder params = new StringBuilder();
				while(names.hasMoreElements()){
					String name = names.nextElement();
					params.append(name+":");
					String[] values=request.getParameterValues(name);
					params.append("[");
					for (int i = 0; i < values.length; i++) {
						if(i>0){
							params.append(",");
						}
						params.append(values[i]);
					}
					params.append("]");
				}
				debugInfo("parameters:"+params.toString());
			}
			debugInfo("action: " + handler.getClass().getSimpleName());
			debugInfo("request encoding: " + request.getCharacterEncoding());
		}
		return super.preHandle(request, response, handler);
	}

	/**
	 * 进入view前执行
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (debugModel) {
			if (modelAndView != null) {
				debugInfo("view: " + modelAndView.getViewName());
			}
			debugInfo("response encoding: " + response.getCharacterEncoding());
		}
		super.postHandle(request, response, handler, modelAndView);
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	private void debugInfo(String message) {
		System.out.println(message);
	}
	static{
		logger.info("system.debugModel:{}", debugModel);
	}
}
