/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * UserController.java
 * 2014-4-22
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.lang.model.element.Element;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ustc.sse.model.User;
import ustc.sse.service.UserService;

/**
 * 实现功能： 对用户的增删改查
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-4-22	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
@Controller("user")
public class UserController {
	private final static Log log = LogFactory.getLog(UserController.class);
	@Resource
	private UserService userService;
	private String userName;
	

	public UserController() {
		super();
	}
	
	/**
	 * 显示用户资料
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("userInfo")
	public String queryUser(HttpServletRequest request,HttpServletResponse response){
		userName = (String) request.getSession().getAttribute("userName");
		if (null != userName) {
			User user = this.getUserService().selectUserByName(userName);
			if (null != user) {
				request.setAttribute("user", user);
			}else {
				return "index";
			}
		}else {
			return "index";
		}
		return "userInfo";
	}
	
	/**
	 * 跳转到更新用户页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateUserLink")
	public String updateUserLink(HttpServletRequest request,HttpServletResponse response){
		return "updateUser";
	}
	/**
	 * 更新用户
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateUser")
	public String updateUser(HttpServletRequest request,HttpServletResponse response){
		userName = (String) request.getSession().getAttribute("userName");
		if (null != userName) {
			log.info("您尚未登录！");
			return "index";
		}
		String pwd = request.getParameter("password");
		String email = request.getParameter("email");
		User user = new User();
		user.setUserName(userName);
		user.setPassword(pwd);
		user.setEmail(email);
		int result = this.getUserService().updateUser(user);
		if (1 == result) {
			log.info("更新用户成功！");
		}else {
			log.error("更新用户失败！");
			return "operateFailed";
		}
		return "operateSuccess";
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}

