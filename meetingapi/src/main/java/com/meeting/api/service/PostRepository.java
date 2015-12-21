package com.meeting.api.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.meeting.api.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post>{

}
