package com.meeting.api.bean;

public class MeetingListBean {

	protected long meeting_id;
	protected String meeting_title; //会议标题
	protected String meeting_address;//会议地点
	protected long meeting_timestamp;//会议时间
	protected long created_timestamp;//发起时戳
	public long getMeeting_id() {
		return meeting_id;
	}
	public void setMeeting_id(long meeting_id) {
		this.meeting_id = meeting_id;
	}
	public String getMeeting_title() {
		return meeting_title;
	}
	public void setMeeting_title(String meeting_title) {
		this.meeting_title = meeting_title;
	}
	public String getMeeting_address() {
		return meeting_address;
	}
	public void setMeeting_address(String meeting_address) {
		this.meeting_address = meeting_address;
	}
	public long getMeeting_timestamp() {
		return meeting_timestamp;
	}
	public void setMeeting_timestamp(long meeting_timestamp) {
		this.meeting_timestamp = meeting_timestamp;
	}
	public long getCreated_timestamp() {
		return created_timestamp;
	}
	public void setCreated_timestamp(long created_timestamp) {
		this.created_timestamp = created_timestamp;
	}
}
