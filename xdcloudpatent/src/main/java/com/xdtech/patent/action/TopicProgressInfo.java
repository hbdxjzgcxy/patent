package com.xdtech.patent.action;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class TopicProgressInfo {
	private int fileCount; //总文件数量
	private int okCount; //处理完成的文件上数量
	private String current; //当前文件
	private int currentSize; //当前文件数据总数
	private int currentOK; //已推送数完成量
	private int cursor;
	private List<String> files = new ArrayList<String>();

	public int getFileCount() {
		return fileCount;
	}

	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	public int getOkCount() {
		return okCount;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
		this.cursor++;
	}

	public int getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(int current_size) {
		this.currentSize = current_size;
	}

	public int getCurrentOK() {
		return currentOK;
	}

	public void setCurrentOK(int currentOK) {
		this.currentOK = currentOK;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public void addOkCount() {
		this.okCount++;
	}

	public int getCursor() {
		return this.cursor;
	}

	public String getProgress() {
		return PERCENT_FORMATOR.format(okCount * 1.0f / fileCount);
	}

	private static NumberFormat PERCENT_FORMATOR = new DecimalFormat("##%");
}
