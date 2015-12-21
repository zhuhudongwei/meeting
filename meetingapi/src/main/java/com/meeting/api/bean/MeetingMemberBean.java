package com.meeting.api.bean;

public class MeetingMemberBean {

	protected long member_id;    //会议参与人标识
	
	protected long user_id;      //参与人标识
	
	protected String member_name;//会议参与人名称
	
	protected long meeting_id;   //会议标识
	
	protected short member_status;//参与状态
	
	protected short assign_status;//指标标识
	
	protected Long assign_id;     //指派人标识
	
	protected String assign_name; //指派人姓名
	
	protected short entruts_status;//委托标识
	
	protected Long entrust_id;    //委托人标识
	
	protected String entrust_name;//委托人姓名
	
	protected String processing;  //处理意见
	
	protected Long processing_timestamp;
	
	protected String mobilephone;

	public long getMember_id() {
		return member_id;
	}

	public void setMember_id(long member_id) {
		this.member_id = member_id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	public long getMeeting_id() {
		return meeting_id;
	}

	public void setMeeting_id(long meeting_id) {
		this.meeting_id = meeting_id;
	}

	public short getMember_status() {
		return member_status;
	}

	public void setMember_status(short member_status) {
		this.member_status = member_status;
	}

	public short getAssign_status() {
		return assign_status;
	}

	public void setAssign_status(short assign_status) {
		this.assign_status = assign_status;
	}

	public Long getAssign_id() {
		return assign_id;
	}

	public void setAssign_id(Long assign_id) {
		this.assign_id = assign_id;
	}

	public String getAssign_name() {
		return assign_name;
	}

	public void setAssign_name(String assign_name) {
		this.assign_name = assign_name;
	}

	public short getEntruts_status() {
		return entruts_status;
	}

	public void setEntruts_status(short entruts_status) {
		this.entruts_status = entruts_status;
	}

	public Long getEntrust_id() {
		return entrust_id;
	}

	public void setEntrust_id(Long entrust_id) {
		this.entrust_id = entrust_id;
	}

	public String getEntrust_name() {
		return entrust_name;
	}

	public void setEntrust_name(String entrust_name) {
		this.entrust_name = entrust_name;
	}

	public String getProcessing() {
		return processing;
	}

	public void setProcessing(String processing) {
		this.processing = processing;
	}

	public Long getProcessing_timestamp() {
		return processing_timestamp;
	}

	public void setProcessing_timestamp(Long processing_timestamp) {
		this.processing_timestamp = processing_timestamp;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	
	
}
