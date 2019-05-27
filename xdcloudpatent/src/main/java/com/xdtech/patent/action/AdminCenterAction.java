package com.xdtech.patent.action;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.xdtech.patent.entity.Log;
import com.xdtech.patent.entity.PatentDB;
import com.xdtech.patent.entity.Role;
import com.xdtech.patent.entity.SearchHistory;
import com.xdtech.patent.entity.User;
import com.xdtech.patent.model.TopicModel;
import com.xdtech.patent.service.TopicService;
import com.xdtech.patent.service.UserService;
import com.xdtech.util.FileSize;

/**
 * 管理中心 action
 * @author changfei
 *
 */
@Controller
@RequestMapping("adminCenter")
@SessionAttributes("roles")
public class AdminCenterAction extends BaseAction {

	String cur = "userCenter";
	List<Role> roles = null;

	@Resource
	private UserService userService;
	@Resource
	private TopicService topicService;

	@RequestMapping("index")
	public ModelAndView index(ModelMap model) {
		File userDir = getUserBaseDir();
		ModelAndView mv = new ModelAndView("adminCenter/index");
		//		if(roles == null)
		roles = userService.findAll(Role.class);

		String space = "0";

		List<PatentDB> dbs = topicService.findSpecificUserDB(getCurrentUser(), DEFAULT_ROLE);
		float size = 0.0f;
		try {
			size = new FileSize().sizeOfDirectory(userDir) /(1024 * 1024f);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		space = String.valueOf(NumberFormat.getInstance().format(size));
		model.addAttribute("space", space);
		model.addAttribute("dbCount", dbs.size());
		model.addAttribute("roles", roles);

		List<TopicModel> voList = new ArrayList<TopicModel>();
		for (PatentDB db : dbs) {
			TopicModel tv = new TopicModel();
			tv.setDb(db);
			tv.setLoginCount(userService.loginLogCount(db.getUser().getId()));
			tv.setLoginEndTime(userService.loginLog(db.getUser().getId()).getTime());
			tv.setPatentCount(db.getCount());

			File topicFolder = new File(userDir, db.getName());
			if (topicFolder.exists()) {
				try {
					size = new FileSize().sizeOfDirectory(topicFolder) / (1024 * 1024f);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				tv.setSpace(String.valueOf(NumberFormat.getInstance().format(size)));
			} else
				tv.setSpace("0");

			voList.add(tv);
		}
		mv.addObject("voList", voList);
		mv.addObject("cur", "admin_center");
		return mv;
	}

	/**
	 * 系统管理-账号管理
	 * @return
	 */
	@RequestMapping("account")
	public ModelAndView account() {
		ModelAndView mv = new ModelAndView("sys/account");
		List<User> users = userService.findAll(User.class);
		for (User u : users) {
			u.setLoginCount(userService.loginLogCount(u.getId()));
		}
		mv.addObject("users", users);
		mv.addObject("cur", "admin_center");
		mv.addObject("sel", "account");
		return mv;
	}

	/**
	 * 创建角色
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/addRole")
	public @ResponseBody String addRole(@ModelAttribute("role") Role role) {
		String msg = "success";
		Long count = userService.getCount("SELECT COUNT(id) FROM Role WHERE name=?", role.getName());
		if (count <= 0) {
			userService.save(role);
		} else {
			msg = "角色名称已经使用";
		}
		return msg;
	}

	/**
	 * 系统管理-角色权限管理
	 * @return
	 */
	@RequestMapping("role")
	public ModelAndView role() {
		ModelAndView mv = new ModelAndView("sys/role");
		List<Role> users = userService.findAll(Role.class);
		mv.addObject("roles", users);
		mv.addObject("cur", "admin_center");
		mv.addObject("sel", "role");
		return mv;
	}

	/**
	 * 修改用户信息
	 * @param req
	 * @return
	 */
	@RequestMapping("/modify")
	public ModelAndView modifyUserInfo(HttpServletRequest req) {
		String id = req.getParameter("id");
		User user = userService.findById(User.class, Integer.parseInt(id));
		return new ModelAndView("/sys_user_modify_info").addObject("mUser", user);
	}

	/**
	 * 修改角色信息
	 * @param req
	 * @return
	 */
	@RequestMapping("/modifyRole")
	public ModelAndView modifyRoleInfo(@RequestParam(value = "id") String id) {
		Role role = userService.findById(Role.class, Integer.parseInt(id));
		return new ModelAndView("/sys_role_modify_info").addObject("role", role).addObject("sel", "role");
	}

	/**
	 * 删除角色信息
	 * @param req
	 * @return
	 */
	@RequestMapping("/deleteRole")
	public @ResponseBody String delRole(@RequestParam(value = "id") Integer id) {
		String msg = "success";
		Long count = userService.getCount("SELECT count(*) FROM User WHERE role.id=?", id);
		if (count <= 0) {
			userService.delete(id, Role.class);
		} else {
			msg = "角色已经被使用，无法删除。";
		}
		return msg;
	}

	/**
	 * 修改角色信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping("/update")
	public @ResponseBody String update(@RequestParam(value = "id") Integer id, Role role) {
		String msg = "success";
		try {
			userService.update(role);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	@ModelAttribute
	public void settingInfoHelp(@RequestParam(value = "id", required = false) Integer id, Map<String, Object> map) {
		if (id != null) {
			map.put("role", userService.findById(Role.class, id));
		}
	}

	/**
	 * 子账号
	 * @param req
	 * @return
	 */
	@RequestMapping("/child")
	public ModelAndView child() {
		User user = getCurrentUser();
		int pid = 0;
		if (!"admin".equals(user.getUsername())) {
			pid = user.getId();
		}
		List<User> users = userService.getChildUsers(pid);
		for (User u : users) {
			u.setLoginCount(userService.loginLogCount(u.getId()));
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subUsers", users);
		params.put("sel", "sub");
		params.put("cur", "admin_center");
		return mv("sys/account/sub", params);
	}

	/**
	 * 检索历史
	 * @param req
	 * @return
	 */
	@RequestMapping("/history")
	public ModelAndView searchHistory(@ModelAttribute("params") SearchForm model) {
		User user = getCurrentUser();
		List<SearchHistory> history = userService.getSearchRecoreds(user.getId(), model);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("history", history);
		params.put("s_total", userService.getSearchRecoredCount(user.getId(), model));
		params.put("sel", "history");
		params.put("cur", "admin_center");
		return mv("search/history", params);
	}

	/**
	 * 用户日志
	 * @return
	 */
	@RequestMapping("/logs")
	public ModelAndView logs(@ModelAttribute("params") SearchForm model) {
		User user = getCurrentUser();
		List<Log> logs = userService.logs(user.getId(), model);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("logs", logs);
		params.put("s_total", userService.logCount(0, model));
		params.put("sel", "log");
		params.put("cur", "admin_center");
		return mv("user/logs", params);
	}
	
	@RequestMapping("/param")
	public ModelAndView paramSetting(){
		ModelAndView mv = new ModelAndView("sys/param");
		mv.addObject("cur", "admin_center");
		mv.addObject("sel", "param");
		return mv;
	}
	

	private ModelAndView mv(String url, Map<String, Object> params) {
		ModelAndView mv = new ModelAndView(url);
		for (Object obj : params.keySet()) {
			mv.addObject("" + obj + "", params.get(obj));
		}
		return mv;
	}
}
