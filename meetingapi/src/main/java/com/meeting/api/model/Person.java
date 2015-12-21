package com.meeting.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="nw_organization_person_tbl")
public class Person {

	public static short STATUS_OPENED = 1;//打开
	public static short STATUS_CLOSED = 2;//关闭
	
	@Column
    @Id
	private Long id;
	@Column
	private String personName;
	@Column
	private String personNo;
	@Column
	private Long departmentId;
	@Column
	private Long postId;
	@Column
	private String mobilephone;
	@Column
	private String email;
	@Column
	private String inphone;
	@Column
	private String outphone;
	@Column
	private String roomNo;
	@Column
	private String seatNo;
	@Column
	private short status;
	@Column
	private String comment;
	
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPersonNo() {
		return personNo;
	}
	public void setPersonNo(String personNo) {
		this.personNo = personNo;
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public Long getPostId() {
		return postId;
	}
	public void setPostId(Long postId) {
		this.postId = postId;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getInphone() {
		return inphone;
	}
	public void setInphone(String inphone) {
		this.inphone = inphone;
	}
	public String getOutphone() {
		return outphone;
	}
	public void setOutphone(String outphone) {
		this.outphone = outphone;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public String getSeatNo() {
		return seatNo;
	}
	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}
	public short getStatus() {
		return status;
	}
	public void setStatus(short status) {
		this.status = status;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
