package com.xdtech.patent.upload;

import java.text.DecimalFormat;

import org.apache.velocity.runtime.parser.node.MathUtils;

import com.xdtech.util.JsonUtil;

public class Progress {

	/** 已读字节 **/
	private long bytesRead = 0L;
	/** 已读MB **/
	private String mbRead = "0";
	/** 总长度 **/
	private long contentLength = 0L;
	/****/
	private int items;
	/** 已读百分比 **/
	private int percent;
	/** 读取速度 **/
	private String speed;
	/** 开始读取的时间 **/
	private long startReatTime = System.currentTimeMillis();

	public long getBytesRead() {
		return bytesRead;
	}

	public void setBytesRead(long bytesRead) {
		this.bytesRead = bytesRead;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public int getItems() {
		return items;
	}

	public void setItems(int items) {
		this.items = items;
	}

	public String getPercent() {
		float d = bytesRead / (contentLength * 1.0f);
		return new DecimalFormat("##%").format(d);
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public String getSpeed() {
		long takeTime = System.currentTimeMillis() - startReatTime;
		takeTime = Math.max(takeTime, 1);
		speed = MathUtils.divide(MathUtils.divide(bytesRead * 1000, takeTime), 1000).toString();
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public long getStartReatTime() {
		return startReatTime;
	}

	public void setStartReatTime(long startReatTime) {
		this.startReatTime = startReatTime;
	}

	public String getMbRead() {
		Number result = MathUtils.divide(bytesRead, 1024 * 1024);
		mbRead = result.toString();
		return mbRead;
	}

	public void setMbRead(String mbRead) {
		this.mbRead = mbRead;
	}

	@Override
	public String toString() {
		return JsonUtil.toJsonStr(this);
	}
}
