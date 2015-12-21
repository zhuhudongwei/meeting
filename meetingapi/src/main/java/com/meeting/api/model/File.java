package com.meeting.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="nw_meeting_file_tbl")
public class File {

	public static short STATUS_CREATED = 0; //新建
	public static short STATUS_REMOVED = 1 ;//删除
	@Column
    @Id
	private Long id;
	@Column
	private String name;
	@Column
	private String ext;
	@Column
	private short status;
	@Column
	private String path;
	@Column
	private String fileName;
	@Column
	private String filePath;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
		StringBuilder sb = new StringBuilder();
		if(null != this.id){
			String _id = this.id.toString();
			String _ids = "0000000000000000000";
			if(_id.length() < 19){
				_ids = _ids.substring(0, _ids.length()-_id.length());
				_ids += _id;
			}
			sb.append(_ids.substring(0, 6));
			sb.append(java.io.File.separatorChar);
			sb.append(_ids.substring(6, 12));
			path = sb.toString();
			sb.append(java.io.File.separatorChar);
			fileName = _ids.substring(12, 19)+"."+ext;
			sb.append(fileName);
			filePath = sb.toString().replaceAll("\\"+java.io.File.separatorChar+"", "/");
		}
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
	public short getStatus() {
		return status;
	}
	public void setStatus(short status) {
		this.status = status;
	}
	
	public String getFilePath() {
		return filePath;
	}
	public String getPath(){
		return path;
	}
	public String getFileName(){
		return fileName;
	}
}
