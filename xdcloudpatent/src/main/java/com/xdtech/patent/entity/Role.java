package com.xdtech.patent.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "cp_role")
public class Role implements Serializable {

	private static final long serialVersionUID = 1685846079600949791L;
	private int id;
	private String code;
	private String name;
	private String desc;
	private int space = 0;
	private int dbCount = 0;

	@Column(name = "R_CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "R_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "R_DESC")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "R_SPACE")
	public int getSpace() {
		return space;
	}

	public void setSpace(int space) {
		this.space = space;
	}

	@Column(name = "R_DB_COUNT")
	public int getDbCount() {
		return dbCount;
	}

	public void setDbCount(int dbCount) {
		this.dbCount = dbCount;
	}

	@Id
	@GeneratedValue(generator = "role_seq")
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Transient
	public boolean isAdmin() {
		return "admin".equals(this.code);
	}

	@Transient
	public boolean isCompany() {
		return "company".equals(this.code);
	}

	@Transient
	public boolean isCompanyChild() {
		return "company_child".equals(this.code);
	}

}
