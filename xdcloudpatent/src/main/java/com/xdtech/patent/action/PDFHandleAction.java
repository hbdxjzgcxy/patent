package com.xdtech.patent.action;

import java.io.File;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xdtech.patent.reader.Util;
import com.xdtech.patent.task.TransferPdfTask;
import com.xdtech.util.ResourceUtil;

@RequestMapping("/pdf")
@Controller
public class PDFHandleAction extends BaseAction {
	@RequestMapping("show")
	public ModelAndView show() {
		//String path = PatentReader.PDF_RESOUSES_PATH;
		String path = getPdfTmpDir();
		ModelAndView mv = new ModelAndView("pdf/handle");
		mv.addObject("filePath", path);
		mv.addObject("sel", "pdf");
		mv.addObject("cur", "admin_center");
		return mv;
	}

	@RequestMapping("handle")
	public @ResponseBody String handle() {
		String msg = "success";
		String userPdfDir = getUserPdfDir();
		new Thread(new TransferPdfTask(userPdfDir)).start();
		return msg;
	}

	private String getPdfTmpDir() {
		String path = getUserPdfDir();
		File file = new File(path, "tmp");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

	private String getUserPdfDir() {
		String path = ResourceUtil.getUserResourceDir(currentUserName(), ResourceUtil.RESOURCE_PATENT_PDF, true);
		return path;
	}
}
