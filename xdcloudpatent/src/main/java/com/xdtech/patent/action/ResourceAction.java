package com.xdtech.patent.action;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.io.Files;
import com.xdtech.util.ResourceUtil;

/**
 * 系统资源访问
 * @author changfei
 *
 */
@Controller
public class ResourceAction extends BaseAction {
	private ResponseEntity<Object> RES_NOT_FOUND = new ResponseEntity<>(HttpStatus.NOT_FOUND);

	/**
	 * 获取专利附图
	 * @param pn
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("resources/patent/img")
	public ResponseEntity<?> getPatentImg(String pn) throws IOException {
		ResponseEntity<Object> response = RES_NOT_FOUND;
		String path = ResourceUtil.getPatentImg(getCurrentUser().getUsername() + "", pn);
		if (path != null) {
			File file = new File(path);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(getMediaType(file));
			String encodedName = new String(file.getName().getBytes(), "ISO8859-1");
			header.set("Content-Disposition", "attachment;filename=" + encodedName);
			byte[] bytes = FileUtils.readFileToByteArray(file);
			response = new ResponseEntity<Object>(bytes, header, HttpStatus.CREATED);
		}
		return response;

	}

	/**
	 * 获取专利附图
	 * @param pn
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("resources/patent/pdf")
	public ResponseEntity<?> getPatentPdf(String pn) throws IOException {
		ResponseEntity<Object> response = RES_NOT_FOUND;
		String path = ResourceUtil.getPatentImg(getCurrentUser().getUsername() + "", pn);
		if (path != null) {
			File file = new File(path);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_PDF);
			String encodedName = new String(file.getName().getBytes(), "ISO8859-1");
			header.set("Content-Disposition", "attachment;filename=" + encodedName);
			byte[] bytes = FileUtils.readFileToByteArray(file);
			response = new ResponseEntity<Object>(bytes, header, HttpStatus.CREATED);
		}
		return response;
	}

	/**
	 * 获取专利附图
	 * @param pn
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("resources/photo")
	public ResponseEntity<?> getPhoto() throws IOException {
		ResponseEntity<Object> response = RES_NOT_FOUND;
		String path = ResourceUtil.getUserImage(currentUserName());
		if (StringUtils.isNotEmpty(path)) {
			File file = new File(path);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(getMediaType(file));
			String encodedName = new String(file.getName().getBytes(), "ISO8859-1");
			header.set("Content-Disposition", "attachment;filename=" + encodedName);
			byte[] bytes = FileUtils.readFileToByteArray(file);
			response = new ResponseEntity<Object>(bytes, header, HttpStatus.CREATED);
		}
		return response;

	}

	/*
	 * 识别资源类型
	 */
	private MediaType getMediaType(File file) {
		String extName = Files.getFileExtension(file.getName());
		return new MediaType("image", extName);
	}

}
