package com.xdtech.patent.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 角色管理Action
 * @author changfei
 *
 */
@Controller
public class RoleAction {

	@RequestMapping("/role/add")
	public String index() {

		return "redirect:/role/index";
	}

	
	
	@RequestMapping("/role/remvoe")
	public String remove(String id) {

		return "redirect:/role/index";
	}

}
