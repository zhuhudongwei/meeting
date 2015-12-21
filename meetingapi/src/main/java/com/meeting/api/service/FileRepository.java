package com.meeting.api.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.meeting.api.model.File;

public interface FileRepository extends JpaRepository<File, Long>, JpaSpecificationExecutor<File>{

	@Query("SELECT max(pf.id) FROM File pf")
	long findMaxId();
}
