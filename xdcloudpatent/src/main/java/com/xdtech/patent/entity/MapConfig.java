package com.xdtech.patent.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
public class MapConfig implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int id;
	/**索引名称*/
	private String indexName;
	/**excel标题名称*/
	private String excelName;
	/**分隔符*/
	private String sepa;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getExcelName() {
		return excelName;
	}
	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}
	public String getSepa() {
		return sepa;
	}
	public void setSepa(String sepa) {
		this.sepa= sepa;
	}
}
