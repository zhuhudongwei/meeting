package com.meeting.api.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.meeting.api.model.MeetingFile;

public interface MeetingFileRepository extends JpaRepository<MeetingFile, Long>,JpaSpecificationExecutor<MeetingFile>{

	@Query("SELECT max(pf.file_id) FROM MeetingFile pf")
	long findMaxId();
	
	@Query("select mf from MeetingFile mf where mf.meeting_id = :meeting_id")
	List<MeetingFile> findByMeeting_id(@Param("meeting_id")long meeting_id);
}
