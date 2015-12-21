package com.meeting.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.meeting.api.model.User;
import com.meeting.api.service.UserRepository;

/**
 * 访问控制器--用户管理
 * @author zdw
 *
 */
@RestController
@RequestMapping("/user")
public class UserControllor {

	@Autowired
	private UserRepository userRepositoryImpl;
	
	/***
	 * 会议通登录
	 * @param account
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/loginUser", method = RequestMethod.POST)
	public Map loginUser(String account, String password, HttpServletRequest request){
		Map ma = new HashMap();
		try {
			User user = userRepositoryImpl.findByNameAndPws(account, password);
			ma.put("UserBean", user);
			ma.put("state", 1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ma.put("state", -1);
		}
		return ma;
	}
}
