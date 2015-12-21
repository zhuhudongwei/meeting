package com.meeting.api.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.meeting.api.model.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{

	@Query("SELECT pf FROM User pf where pf.account = :account and pf.password = :password")
	User findByNameAndPws(@Param("account")String account, @Param("password")String password);
}
