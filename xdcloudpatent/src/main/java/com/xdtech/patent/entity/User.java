package com.xdtech.patent.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.codec.digest.DigestUtils;

@Entity
@Table(name = "cp_user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String username;

	private String email;

	private String company;

	private char sex;

	private String phone;

	private String desc;

	private String password;

	private Integer pid = 0;//父用户标识

	private boolean disabled;

	/**用户时间戳*/
	private Date lifeTime;
	private String photo;

	/**是否认证*/
	private int isAuth;

	//	private int role;

	private Role role;

	private int loginCount;

	@Column(name = "U_NAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "U_EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "U_COMP")
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Column(name = "U_SEX")
	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

	@Column(name = "U_TEL")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "U_DESC")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "U_PWD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "M_TIME")
	public Date getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(Date lifeTime) {
		this.lifeTime = lifeTime;
	}

	@Column(name = "U_PHOTO")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Column(name = "U_IS_AUTH")
	public int getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(int isAuth) {
		this.isAuth = isAuth;
	}

	//	@Column(name ="U_ROLE")
	//	public int getRole() {
	//		return role;
	//	}
	//	public void setRole(int role) {
	//		this.role = role;
	//	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "U_ROLE")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Id
	@GeneratedValue(generator = "user_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "U_P_ID")
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Column(name = "U_IS_DISABLED")
	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	@Transient
	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public void restPwd() {
		this.password = DigestUtils.md5Hex("123456");
	}

}
