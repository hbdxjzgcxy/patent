package com.xdtech.patent.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 导入配置项管理
 * @author coolBoy
 *
 */
@Entity
@Table(name="import_cfg")
public class ImportTemplete {
	
	private int id;
	
	/**
	 * 使用的配置我文件名称
	 */
	private String  profileName;
	/**
	 *系统定义
	 */
	private boolean internal;
	/**默认导入配置文件*/
	private boolean activity;
	
	/**创建日期*/
	private Date createDate;
	
	/**配置文件描述*/
	private String  profileDesc;
	
	/**配置文件的存储路径*/
	private String profileUrl;
	
	private String uid;
	
	@Id
	@GeneratedValue(generator = "import_cfg_seq",strategy=GenerationType.SEQUENCE)
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="profile_name")
	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	@Column(name="internal")
	public boolean isInternal() {
		return internal;
	}

	public void setInternal(boolean internal) {
		this.internal = internal;
	}

	@Column(name="activity")
	public boolean isActivity() {
		return activity;
	}

	public void setActivity(boolean activity) {
		this.activity = activity;
	}

	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name="profile_desc")
	public String getProfileDesc() {
		return profileDesc;
	}

	public void setProfileDesc(String profileDesc) {
		this.profileDesc = profileDesc;
	}
	@Column(name="profile_url")
	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}
