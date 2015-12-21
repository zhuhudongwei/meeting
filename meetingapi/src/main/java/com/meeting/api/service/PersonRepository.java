package com.meeting.api.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.meeting.api.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long>,JpaSpecificationExecutor<Person>{

	@Query("select p from Person p where departmentId = :departmentId and p.postId = :postId")
	List<Person> findByDidAndPid(@Param("departmentId") Long departmentId, @Param("postId") Long postId);
}
