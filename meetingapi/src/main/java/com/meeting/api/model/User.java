package com.meeting.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="nw_portal_user_tbl")
public class User {

	public static short ISMANAGER_YES = 1;//是管理员
	public static short ISMANAGER_NO = 0;//不是管理员
	
	@Column
	@Id
	private Long id;
	@Column
	private String account;
	@Column
	private String username;
	@Column
	private String email;
	@Column
	private String telephone;
	@Column
	private String password;
	@Column
	private String icon;
	@Column
	private short ismanager;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public short getIsmanager() {
		return ismanager;
	}
	public void setIsmanager(short ismanager) {
		this.ismanager = ismanager;
	}
}
