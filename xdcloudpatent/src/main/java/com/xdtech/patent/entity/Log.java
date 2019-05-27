package com.xdtech.patent.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="cp_user_log")
public class Log implements Serializable{

	private static final long serialVersionUID = -4992799318795655046L;

	private int id;
	private User user;
	private String action;
	private String desc;
	private Date time;

	public Log(){}
	
	public Log(User user, String action, String desc) {
		super();
		this.user = user;
		this.action = action;
		this.desc = desc;
		this.time = new Date();
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST})
	@JoinColumn(name = "L_USER")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name="L_ACTION")
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	@Column(name="L_DESC")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Column(name="L_TIME")
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	@Id
    @GeneratedValue(generator="log_seq")
    @Column(name = "ID")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
