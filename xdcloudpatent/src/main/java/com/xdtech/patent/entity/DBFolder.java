package com.xdtech.patent.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 专题库下的目录
 * @author Administrator
 *
 */

@Entity
@Table(name = "cp_db_folder")
public class DBFolder implements Serializable{

	private static final long serialVersionUID = 3464228024176115193L;

	private int id;
	private String name;
	private String alias;
	private int parent;
	private int db;
	private int pid;
	private String filter;
	private int level;//节点层级
	private int leaf;//叶子节点1 非叶子节点0
	private int sort;
	
	
	@Column(name = "F_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "F_PARENT")
	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	@Column(name = "F_DB")
	public int getDb() {
		return db;
	}

	public void setDb(int db) {
		this.db = db;
	}

	@Column(name = "PID")
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	@Lob
	@Column(name = "F_FILTER")
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	@Id
	@GeneratedValue(generator = "db_folder_seq",strategy=GenerationType.SEQUENCE)
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="F_LEVEL")
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	@Column(name="F_LEAF")
	public int getLeaf() {
		return leaf;
	}

	public void setLeaf(int leaf) {
		this.leaf = leaf;
	}
	@Column(name="F_ALIAS")
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}

	
	@Column(name = "F_SORT")
	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
