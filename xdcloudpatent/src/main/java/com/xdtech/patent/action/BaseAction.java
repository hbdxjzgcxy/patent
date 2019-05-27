package com.xdtech.patent.action;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.xdtech.SysProperties;
import com.xdtech.patent.conf.AppConf;
import com.xdtech.patent.entity.Log;
import com.xdtech.patent.entity.User;
import com.xdtech.patent.service.CommonService;
import com.xdtech.search.client.XDSearchSupport;

/**
 * 
 * @author Chang Fei
 *
 */
@Controller
public class BaseAction extends XDSearchSupport {
	public static String UPLOAD_PATH = "upload";
	public static String BASE_DIR = SysProperties.USER_DATA_DIR;

	protected User currentUser;

	public static final String AJAX_OK = "true";

	public static final String AJAX_FAIL = "false";

	final static Integer DEFAULT_ROLE = 3;

	public static final String CORE_NAME = AppConf.get().get("core.name", "patent");

	@Resource(name = "commonService")
	public CommonService service;

	/**
	 * 时间格式Editor
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@ModelAttribute
	protected void hookUser(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(SysProperties.CURRENT_USER);
		if (obj != null) {
			currentUser = (User) obj;
		}
	}

	@ModelAttribute
	protected void init(HttpServletRequest request) {
		UPLOAD_PATH = request.getSession().getServletContext().getRealPath("/") + "upload/";
	}

	@ModelAttribute
	protected HttpSession getSession(HttpSession session) {
		return session;
	}

	protected User getCurrentUser() {
		return currentUser;
	}

	protected String currentUserName() {
		return currentUser.getUsername();
	}

	public String getDocNo(int uid, String docNo) {
		return uid + "!" + docNo;
	}

	/**
	 * 统一异常处理
	 * @param req
	 * @param ex
	 * @return
	 */
	@ExceptionHandler
	public String exception(HttpServletRequest req, Exception ex) {
		class Error {
			private String error;

			public String getError() {
				return error;
			}

			public String getErrMsg() {
				return errMsg;
			}

			private String errMsg;

			Error(String error, String errMsg) {
				this.error = error;
				this.errMsg = errMsg;
			}
		}
		Error error = new Error(ex.getClass().getSimpleName(), ex.getMessage());
		ex.printStackTrace();
		ObjectMapper om = new ObjectMapper();
		try {
			String einfo = om.writeValueAsString(error);
			req.setAttribute("ex", einfo);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String header = req.getHeader("X-Requested-With");
		if (header != null && !header.isEmpty()) {
			return "/module/error/ajax_error.jsp";
		} else {
			return "error";
		}
	}

	/**
	 *获取用户文件目录
	 */
	public File getUserBaseDir() {
		File uploadFile = new File(BASE_DIR, currentUserName());
		if (!uploadFile.exists()) {
			uploadFile.mkdirs();
		}
		return uploadFile;
	}

	protected String toJsonStr(Object obj) {
		StringWriter writer = new StringWriter();
		try {
			JsonGenerator generator = new ObjectMapper().getJsonFactory().createJsonGenerator(writer);
			generator.writeObject(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.getBuffer().toString();
	}

	public void log(String action, String desc) {
		Log log = new Log(getCurrentUser(), action, desc);
		service.save(log);
	}

}