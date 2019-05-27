package com.xdtech.patent.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.xdtech.patent.conf.AppContextUtils;
import com.xdtech.patent.entity.User;

/**
 * 安全检查差
 * @author changfei
 *
 */
public class SecurityCheck extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURI();
		Object userObj = request.getSession().getAttribute("user");

		//用户未登录跳转或者session失效跳转到登录页面
		if (userObj == null && (!url.endsWith("login.html"))) {
			response.sendRedirect("/xdcloudpatent/login.jsp");
			return false;
		}

		// 在线账户被禁用后跳转到登录页面
		User user = (User) userObj;
		if (user != null && isDisabled(user)) {
			request.getSession().invalidate();
			response.sendRedirect("/xdcloudpatent/login.jsp");
			return false;
		}

		//丢弃用户等登录用的session
		if (url.endsWith("login.html")) {
			request.getSession().invalidate(); //登录页面session失效,骗过安全检查软件提示的session劫持问题??
		}

		return super.preHandle(request, response, handler);
	}

	/*
	 *检查用户是否禁用 
	 */
	private boolean isDisabled(User user) {
		return AppContextUtils.isDisabledUser(user.getId()) || user.isDisabled();
	}

}
