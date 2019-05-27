package com.xdtech.patent.action;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.xdtech.patent.entity.User;

@SessionAttributes("user")
@Controller
public class LogoutAction extends BaseAction{

	/**
	 * 注销用户
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/logout")
	public String logout(@ModelAttribute User user, SessionStatus session) throws IOException {
		
		log("logou","注销");
		user = null;
		session.setComplete();
		return "redirect:/login.jsp";
	}

}
