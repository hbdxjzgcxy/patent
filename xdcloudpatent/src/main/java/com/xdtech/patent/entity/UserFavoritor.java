package com.xdtech.patent.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="cp_user_favoritor")
public class UserFavoritor implements Serializable{

	private static final long serialVersionUID = -4440252432560733754L;

	private int id;
	private int user;
	private String docNo;
	private String core;
	private String folder;
	private Date date;
	
	@Column(name="F_USER")
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user = user;
	}
	
	@Column(name="F_DOCNO")
	public String getDocNo() {
		return docNo;
	}
	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}
	
	@Column(name="F_CORE")
	public String getCore() {
		return core;
	}
	public void setCore(String core) {
		this.core = core;
	}
	
	@Column(name="F_FOLDER")
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	@Id
    @GeneratedValue(generator="user_favoritor_seq")
    @Column(name = "ID")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="CREATE_DATE")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
	
}
