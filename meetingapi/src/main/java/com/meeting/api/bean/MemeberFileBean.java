package com.meeting.api.bean;


public class MemeberFileBean {

	protected Long file_id;       //附件标识
	protected long meeting_id;    //会议标识
	protected short file_type;    //附件类型
	protected long file_target_id;//附件目标标识
	protected String file_url;    //附件地址
	private String ext;
	protected String name;
	public Long getFile_id() {
		return file_id;
	}
	public void setFile_id(Long file_id) {
		this.file_id = file_id;
	}
	public long getMeeting_id() {
		return meeting_id;
	}
	public void setMeeting_id(long meeting_id) {
		this.meeting_id = meeting_id;
	}
	public short getFile_type() {
		return file_type;
	}
	public void setFile_type(short file_type) {
		this.file_type = file_type;
	}
	public long getFile_target_id() {
		return file_target_id;
	}
	public void setFile_target_id(long file_target_id) {
		this.file_target_id = file_target_id;
	}
	public String getFile_url() {
		return file_url;
	}
	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	
}
