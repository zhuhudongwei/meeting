package com.meeting.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="nw_organization_post_tbl")
public class Post {
	@Column
    @Id
	private Long id;
	@Column
	private String postName;
	@Column
	private int postLevel;
	@Column
	private String comment;
	
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public int getPostLevel() {
		return postLevel;
	}
	public void setPostLevel(int postLevel) {
		this.postLevel = postLevel;
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
