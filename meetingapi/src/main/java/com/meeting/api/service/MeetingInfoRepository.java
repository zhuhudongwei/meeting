package com.meeting.api.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.meeting.api.model.MeetingInfo;

public interface MeetingInfoRepository extends JpaRepository<MeetingInfo, Long>, JpaSpecificationExecutor<MeetingInfo>{
	
	@Query("SELECT max(pf.meeting_id) FROM MeetingInfo pf")
	long findMaxId();

	@Query("update 	MeetingInfo set meeting_status =:meeting_status where meeting_id = :meeting_id")
	void updateMeetingByStauts(@Param("meeting_id")long meeting_id, @Param("meeting_status")short meeting_status);
}
