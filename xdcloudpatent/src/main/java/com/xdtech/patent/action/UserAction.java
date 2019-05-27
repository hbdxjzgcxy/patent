package com.xdtech.patent.action;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.xdtech.patent.conf.AppContextUtils;
import com.xdtech.patent.entity.Role;
import com.xdtech.patent.entity.User;
import com.xdtech.patent.reader.Util;
import com.xdtech.patent.service.UserService;
import com.xdtech.util.ResourceUtil;

@Controller
@RequestMapping("/user")
public class UserAction extends BaseAction {
	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping("toModifypass")
	public ModelAndView toModifyPass() {
		return new ModelAndView("user/modifypass").addObject("sel", "modify").addObject("cur", "admin_center");
	}

	@RequestMapping("toSettingInfo")
	public ModelAndView toSettingInfo() {
		return new ModelAndView("user/user_setting_info").addObject("sel", "setting").addObject("cur", "admin_center");
	}

	/**
	 * 密码修改
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping("/modifypass")
	public @ResponseBody String modifyPass(@RequestParam(value = "oldpass") String oldpass, @RequestParam(value = "password") String password,
			ModelMap model) {
		User user = getCurrentUser();
		String msg = "success";
		if (!StringUtils.isEmpty(oldpass) && !StringUtils.isEmpty(password)) {
			if (user.getPassword().equals(DigestUtils.md5Hex(oldpass))) {
				User u = userService.findById(User.class, user.getId());
				u.setPassword(DigestUtils.md5Hex(password));
				userService.update(u);
				// session.removeAttribute("user");
				// session.setAttribute("user", u);
			} else {
				msg = "原密码不正确";
			}
		} else {
			msg = "请输入必填项";
		}

		log("modify pass", "修改密码");
		return msg;
	}

	/**
	 * 充值密码
	 * @param uid
	 * @return
	 */
	@RequestMapping("restPwd")
	public @ResponseBody String restPassword(int uid) {
		String msg = "success";
		if (currentUser.getRole().isAdmin()) {
			User user = service.findById(User.class, uid);
			if (null != user) {
				user.restPwd();
				service.update(user);
			} else {
				msg = "用户不存在";
			}
		} else {
			msg = "拒绝访问!";
		}
		return msg;
	}

	/**
	 * 禁用用户
	 * @param uid
	 * @return
	 */
	@RequestMapping("disable")
	public @ResponseBody String disable(int uid) {
		String msg = "success";
		if (currentUser.getRole().isAdmin()) {
			User user = service.findById(User.class, uid);
			if (null != user) {
				user.setDisabled(true);
				service.update(user);
				AppContextUtils.addDisabledUser(uid);
			} else {
				msg = "用户不存在";
			}
		} else {
			msg = "拒绝访问!";
		}
		return msg;
	}

	/**
	 * 启用用户
	 * @param uid
	 * @return
	 */
	@RequestMapping("enable")
	public @ResponseBody String enabled(int uid) {
		String msg = "success";
		if (currentUser.getRole().isAdmin()) {
			User user = service.findById(User.class, uid);
			if (null != user) {
				user.setDisabled(false);
				service.update(user);
				AppContextUtils.removeDisabledUser(uid);
			} else {
				msg = "用户不存在";
			}
		} else {
			msg = "拒绝访问!";
		}
		return msg;
	}

	@ModelAttribute
	public void settingInfoHelp(@RequestParam(value = "id", required = false) Integer id, Map<String, Object> map) {
		if (id != null) {
			map.put("user", userService.findById(User.class, id));
		}
	}

	/**
	 * 修改用户信息
	 * @param req
	 * @return
	 * @throws FileUploadException 
	 * @throws IOException 
	 */
	@RequestMapping("/settingInfo")
	public @ResponseBody String settingInfo(HttpServletRequest request, @RequestParam(value = "user.role.id") Integer id, User user)
			throws IOException, FileUploadException {
		String msg = "success";
		try {
			user.setPhoto(photo(request, user.getUsername()));
			user.setRole(userService.findById(Role.class, id));
			userService.update(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 创建用户
	 * @param user
	 * @return
	 * @throws FileUploadException 
	 * @throws IOException 
	 */
	@RequestMapping("/add")
	public @ResponseBody String addUser(HttpServletRequest request, User user) throws IOException, FileUploadException {
		String msg = "success";
		User tmp = userService.getUser(user.getUsername());
		DateTime dt = DateTime.now();
		if (tmp == null) {
			user.setPassword(DigestUtils.md5Hex(user.getPassword()));
			user.setLifeTime(dt.toDate());
			user.setPhoto(photo(request, user.getUsername()));
			userService.save(user);
		} else {
			msg = "用户信息已经注册";
		}

		log("create account", "创建账号");
		return msg;
	}

	/**
	 * 删除用户
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	public @ResponseBody String deleteUser(@RequestParam(value = "id") Integer id) {
		String msg = "success";
		List<User> subUsers = userService.getChildUsers(id);
		if (subUsers != null && !subUsers.isEmpty()) {
			return "faild";
		}
		userService.delete(id, User.class);
		log("delete account", "删除账号");
		return msg;
	}

	/**
	 * 目录
	 * @param request
	 * @param username 文件夹的父级目录
	 * @return
	 * @throws IOException
	 * @throws FileUploadException
	 */
	public String photo(HttpServletRequest request, String username) throws IOException, FileUploadException {
		//File photoDir = new File(Util.createWebPath("user/photo"));
		String imgDir = ResourceUtil.getUserImageDir(currentUserName());
		File photoDir = new File(imgDir);
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		String filename = "";
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			File uploadFile = null;
			while (iter.hasNext()) {
				MultipartFile multiFile = multiRequest.getFile(iter.next());
				if (!multiFile.isEmpty()) {
					String originalName = multiFile.getOriginalFilename();
					String extension = FilenameUtils.getExtension(originalName);
					if ("PNG".equals(extension.toUpperCase()) || "JPG".equals(extension.toUpperCase())) {
						filename = username + "." + extension;
						uploadFile = new File(photoDir, filename);
						if (!uploadFile.getParentFile().exists())
							uploadFile.getParentFile().mkdirs();
						try {
							FileUtils.copyInputStreamToFile(multiFile.getInputStream(), uploadFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return filename;
	}
}
