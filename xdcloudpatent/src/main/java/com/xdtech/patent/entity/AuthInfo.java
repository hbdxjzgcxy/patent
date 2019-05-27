package com.xdtech.patent.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cp_auth_inf")
public class AuthInfo {

	private static final long serialVersionUID = 6745964797148402763L;

	private int id;
	private String busLic;
	private String applyFile;
	private int applyUser;

	@Column(name = "A_BUS_LIC")
	public String getBusLic() {
		return busLic;
	}

	public void setBusLic(String busLic) {
		this.busLic = busLic;
	}

	@Column(name = "A_APPLY_FILE")
	public String getApplyFile() {
		return applyFile;
	}

	public void setApplyFile(String applyFile) {
		this.applyFile = applyFile;
	}

	@Column(name = "A_APPLY_USER")
	public int getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(int applyUser) {
		this.applyUser = applyUser;
	}

	@Id
	@GeneratedValue(generator = "auth_info_seq")
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
