package com.xdtech.patent.model;

import java.util.Date;

import com.xdtech.patent.entity.PatentDB;

public class TopicModel {
	
	private PatentDB db;
	private long loginCount;
	private Date loginEndTime;
	private String space;
	private int patentCount;
	public PatentDB getDb() {
		return db;
	}
	public void setDb(PatentDB db) {
		this.db = db;
	}
	public long getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(long loginCount) {
		this.loginCount = loginCount;
	}
	public Date getLoginEndTime() {
		return loginEndTime;
	}
	public void setLoginEndTime(Date loginEndTime) {
		this.loginEndTime = loginEndTime;
	}
	public String getSpace() {
		return space;
	}
	public void setSpace(String space) {
		this.space = space;
	}
	public int getPatentCount() {
		return patentCount;
	}
	public void setPatentCount(int patentCount) {
		this.patentCount = patentCount;
	}
	
	
	
}
