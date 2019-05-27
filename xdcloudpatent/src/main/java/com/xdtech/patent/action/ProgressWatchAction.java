package com.xdtech.patent.action;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xdtech.patent.upload.Progress;

/**
 * 服务端进度监控
 * 
 * @author ChangFei
 *
 */
@Controller
@RequestMapping("/progress")
public class ProgressWatchAction {

	/**
	 * 文件上传进度
	 * @param session
	 * @return
	 */
	@RequestMapping("/getupload")
	@ResponseBody
	public String get(HttpSession session) {
		Progress progress = (Progress) session.getAttribute("upload_ps");
		if (progress != null) {
			return progress.toString();
		} else {
			return "xxxxxxxxx";
		}
	}

	/**
	 * 刷新推送到session中的专题处理(推送)进度。
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping("/topic")
	public ModelAndView getTopicProgress(HttpSession session, @RequestParam(defaultValue = "ul-bar") String id) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("topic_push_message");
		mv.addObject("id", id);
		return mv;
	}

}
