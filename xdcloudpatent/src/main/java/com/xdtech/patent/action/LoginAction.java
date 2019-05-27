package com.xdtech.patent.action;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.xdtech.patent.entity.User;
import com.xdtech.patent.service.UserService;

@Controller
@RequestMapping("/login")
@SessionAttributes("user")
public class LoginAction extends BaseAction {
	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 用户登录
	 * @param password
	 * @param loginPara
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/login")
	public @ResponseBody String login(@RequestParam(value = "password") String password, @RequestParam(value = "username") String loginPara,
			ModelMap model) throws IOException {
		User user = userService.getUser(loginPara);
		if (user != null && !user.isDisabled()) {
			password = DigestUtils.md5Hex(password);
			if (user.getPassword().equals(password)) {
				model.addAttribute("user", user);
				log("login", "登录");
				return "true";
			}
		}
		// res.getWriter().print(false);

		return "false";
	}

	@RequestMapping("cookielogin")
	public ModelAndView cookLogin(String user_ssl_key, String user_ssl_pk, ModelMap model) {
		User user = userService.getUser(user_ssl_key);
		model.addAttribute("user", user);
		return new ModelAndView("redirect:/smart_search.html");
	}

	/**
	 * 获取用户解密的KEY
	 * @return
	 */
	@RequestMapping("user_pk")
	@ResponseBody
	public String getUserPK() {
		return DigestUtils.shaHex(currentUser.getPassword()) + ":/FK-2=" + UUID.randomUUID().getLeastSignificantBits();
	}

}
