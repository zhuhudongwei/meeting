package com.meeting.api.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.meeting.api.model.MeetingMember;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long>, JpaSpecificationExecutor<MeetingMember>{

	@Query("SELECT max(pf.member_id) FROM MeetingMember pf")
	long findMaxId();
	
	@Query("select mm from MeetingMember mm where mm.meeting_id = :meeting_id")
	List<MeetingMember> findByMeeting_id(@Param("meeting_id")long meeting_id);
	
	@Query("select mm from MeetingMember mm where mm.meeting_id = :meeting_id and mm.user_id = :user_id")
	MeetingMember findByMeeting_idAndUser_id(@Param("meeting_id")long meeting_id, @Param("user_id")long user_id);
}
