package com.xdtech.patent.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 *  专利数据导入日志
 * @author Administrator
 *
 */
@Entity
@Table(name="cp_db_log")
public class DataLog implements Serializable{

	private static final long serialVersionUID = 8184551633189277322L;
	private int id;
	private int rowNum;
	private String filepath;
	private String filename;
	private String info;
	private int  db;
	private String desc;

	
	
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	@Column(name="D_FILENAME")
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Lob
	@Column(name="L_INFO")
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	@Column(name="D_DB")
	public int getDb() {
		return db;
	}
	public void setDb(int db) {
		this.db = db;
	}
	
	@Lob
	@Column(name="D_DESCRIPT")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Column(name="D_FILEPATH")
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
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
