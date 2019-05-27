package com.xdtech.patent.task;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.xdtech.patent.action.SearchBean;

/**
 * 专利PDF路径迁移程序。 根据利的申请号拆分pdf路径。
 * 
 * @author coolBoy
 *
 */
public class TransferPdfTask implements Runnable {
	static volatile boolean end = true;

	private String userPdf;

	public void run() {
		if (end) {
			try {
				end = false;
				String path = userPdf;
				File file = new File(path);
				if (!file.exists()) {
					file.mkdirs();
				}
				file = new File(file, "tmp");
				if (!file.exists()) {
					file.mkdirs();
				}
				File[] files = file.listFiles();
				for (File f : files) {
					transfer(file.getParentFile(), f);
				}
			} finally {
				end = true;
			}
		}
	}

	public void transfer(File dest, File f) {
		if (f.isFile()) {
			String name = f.getName().toUpperCase();
			if (name.endsWith(".PDF")) {
				String PN = FilenameUtils.getBaseName(name);
				String subPath = SearchBean.pn2Path(PN);
				File destDir = new File(dest, subPath);
				destDir.mkdirs();
				try {
					File destFile = new File(destDir, name);
					if (!destFile.exists())
						FileUtils.moveFile(f, destFile);
					f.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			File[] files = f.listFiles();
			for (File file : files) {
				transfer(dest, file);
			}
		}
	}

	public TransferPdfTask(String pdfPath) {
		this.userPdf = pdfPath;
	}

}
