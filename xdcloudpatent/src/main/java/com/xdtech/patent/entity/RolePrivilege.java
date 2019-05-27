package com.xdtech.patent.entity;

import java.io.Serializable;

import javax.persistence.Column;


//@Entity
//@Table(name="cp_role_privilege")
public class RolePrivilege implements Serializable{
	private static final long serialVersionUID = 1973270628095599446L;

	
	private int rId;
	private int mId;
	
	
	@Column(name="R_ID")
	public int getrId() {
		return rId;
	}
	public void setrId(int rId) {
		this.rId = rId;
	}
	
	@Column(name="M_ID")
	public int getmId() {
		return mId;
	}
	public void setmId(int mId) {
		this.mId = mId;
	}
	
	
}
