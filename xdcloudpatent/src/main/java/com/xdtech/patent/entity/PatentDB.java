package com.xdtech.patent.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * 专题库
 * @author Administrator
 *
 */
@Entity
@Table(name="cp_patent_db")
public class PatentDB implements Serializable{

	private static final long serialVersionUID = 264371779697688706L;
	
	private int id;
	private String name;
	private String alias;
	private User user;
	private String core;
	private int status;//0 未做索引 1 索引完成
	private int count;
	//添加专题字段，控制删除索引时机
	private String topicName;
	
	@Column(name="DB_NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	@Column(name="DB_ALIAS")
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "DB_USER")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name="DB_CORE")
	public String getCore() {
		return core;
	}
	public void setCore(String core) {
		this.core = core;
	}
	
	
	@Id
    @GeneratedValue(generator="patent_db_seq")
    @Column(name = "ID")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="DB_STATUS")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column(name = "F_COUNT")
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	@Column(name = "TOPIC_COUNT")
	public String getTopicName() {
		return topicName;
	}
	
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

}
