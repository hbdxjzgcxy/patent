package com.xdtech.patent.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "cp_search_history")
public class SearchHistory {

	private static final long serialVersionUID = -8379625974144328647L;

	private int id;
	private String word;
	private String query;
	private int user;
	private Date time;
	private String ip;
	private long hitcount;
	private String takeTime;
	private String type;//检索类型

	@Column(name = "S_WORD")
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	@Lob
	@Column(name = "S_QUERY")
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Column(name = "S_USER")
	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	@Column(name = "S_TIME")
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name = "S_IP")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "S_HIT_COUNT")
	public long getHitcount() {
		return hitcount;
	}

	public void setHitcount(long hitcount) {
		this.hitcount = hitcount;
	}

	@Column(name = "S_TAKE_TIME")
	public String getTakeTime() {
		return takeTime;
	}

	public void setTakeTime(String takeTime) {
		this.takeTime = takeTime;
	}
	
	@Column(name="S_SEARCH_TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Id
	@GeneratedValue(generator = "search_history_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
