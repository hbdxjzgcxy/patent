package com.xdtech.patent.upload;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

public class FileuploadListener implements ProgressListener {

	private HttpSession session;

	public FileuploadListener() {
	}

	public FileuploadListener(HttpSession session) {
		this.session = session;
		Progress status = new Progress();
		session.setAttribute("upload_ps", status);
	}

	/**
	 * pBytesRead 到目前为止读取文件的比特数 pContentLength 文件总大小 pItems 目前正在读取第几个文件
	 */
	public void update(long pBytesRead, long pContentLength, int pItems) {
		Progress status = (Progress) session.getAttribute("upload_ps");
		status.setBytesRead(pBytesRead);
		status.setContentLength(pContentLength);
		status.setItems(pItems);
		//System.out.println(status);
		session.setAttribute("upload_ps", status);
	}

}
