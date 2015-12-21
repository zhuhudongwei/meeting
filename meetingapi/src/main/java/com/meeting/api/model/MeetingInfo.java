package com.meeting.api.model;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="nw_meeting_info_tbl")
public class MeetingInfo {
	
	public final static short MEETING_STATUS_NEW = 1;    //未召开
	public final static short MEETING_STATUS_MEETING = 2;//已召开
	public final static short MEETING_STATUS_CANCEL = 9; //取消

	@Column
    @Id
	protected long meeting_id;
	@Column
	protected String meeting_title; //会议标题
	@Column
	protected String meeting_address;//会议地点
	@Column
	protected long meeting_timestamp;//会议时间
	@Column
	protected String meeting_content;//会议说明
	@Column
	protected short meeting_status;  //会议状态
	@Column
	protected long creator_id;       //发起人标识
	@Column
	protected String creator_name;   //发起人姓名
	@Column
	protected long created_timestamp;//发起时戳
	@Column
	protected Long cancel_id;        //取消人标识
	@Column
	protected String cancel_name;    //取消人姓名
	@Column
	protected Long cancel_timestamp; //取消时戳
	@Column
	protected String cancel_content; //取消原因
	
	private String mobilephone;
	
	@OneToMany
	@JoinColumn(name="meeting_id")
	protected List<MeetingMember> meetingMembers;

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
	public String getMeeting_content() {
		return meeting_content;
	}
	public void setMeeting_content(String meeting_content) {
		this.meeting_content = meeting_content;
	}
	public short getMeeting_status() {
		return meeting_status;
	}
	public void setMeeting_status(short meeting_status) {
		this.meeting_status = meeting_status;
	}
	public long getCreator_id() {
		return creator_id;
	}
	public void setCreator_id(long creator_id) {
		this.creator_id = creator_id;
	}
	public String getCreator_name() {
		return creator_name;
	}
	public void setCreator_name(String creator_name) {
		this.creator_name = creator_name;
	}
	public long getCreated_timestamp() {
		return created_timestamp;
	}
	public void setCreated_timestamp(long created_timestamp) {
		this.created_timestamp = created_timestamp;
	}
	public Long getCancel_id() {
		return cancel_id;
	}
	public void setCancel_id(Long cancel_id) {
		this.cancel_id = cancel_id;
	}
	public String getCancel_name() {
		return cancel_name;
	}
	public void setCancel_name(String cancel_name) {
		this.cancel_name = cancel_name;
	}
	public Long getCancel_timestamp() {
		return cancel_timestamp;
	}
	public void setCancel_timestamp(Long cancel_timestamp) {
		this.cancel_timestamp = cancel_timestamp;
	}
	public String getCancel_content() {
		return cancel_content;
	}
	public void setCancel_content(String cancel_content) {
		this.cancel_content = cancel_content;
	}
	public List<MeetingMember> getMeetingMembers() {
		return meetingMembers;
	}
	public void setMeetingMembers(List<MeetingMember> meetingMembers) {
		this.meetingMembers = meetingMembers;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	
}
