package com.meeting.api.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.meeting.api.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>,JpaSpecificationExecutor<Department>{

	@Query("select d from Department d where d.pid = :pid")
	List<Department> findByPid(@Param("pid") long pid);
	
	@Query("select d from Department d where d.pid is null")
	List<Department> selectDepartmentParent();
}
