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
import java.util.List;

import javax.annotation.Resource;
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
 * date	        author            email		           notes<br />
 * ----------------------------------------------------------------<br />
 *2014-2-19      邱星       starqiu@mail.ustc.edu.cn	    新建类<br /></p>
 * 
 */
@Controller
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
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("userInfo")
	public String userInfo(HttpServletRequest request,
			HttpServletResponse response) {
		userName = request.getParameter("userName");//管理员点击
		if (null == userName) {//点击设置修改自身用户资料
			userName = (String) request.getSession().getAttribute("userName");
		}
		if (null != userName) {
			User param = new User();
			param.setUserName(userName);
			User user = this.getUserService().selectUserByName(param);
			if (null != user) {
				request.setAttribute("user", user);
			} else {
				return "index";
			}
		} else {
			return "index";
		}
		return "userInfo";
	}

	/**
	 * 跳转到更新用户页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateUserLink")
	public String updateUserLink(HttpServletRequest request,
			HttpServletResponse response) {
		return "updateUser";
	}
	
	/**
	 * 跳转到更新用户-用户名输入页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateUserByAdminLink")
	public String updateUserByAdminLink(HttpServletRequest request,
			HttpServletResponse response) {
		return "updateUserInput";
	}
	
	/**
	 * 跳转到更新用户-用户信息反显页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateUserInput")
	public String updateUserInput(HttpServletRequest request,
			HttpServletResponse response) {
		userInfo(request, response);
		return "updateUserByAdmin";
	}

	/**
	 * 更新用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateUser")
	public String updateUser(HttpServletRequest request,
			HttpServletResponse response) {
		userName = request.getParameter("userName");//管理员点击修改用户，修改其他用户的资料
		if (null == userName) {//点击设置修改自身用户资料
			userName = (String) request.getSession().getAttribute("userName");
		}
		if (null == userName) {
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
		} else {
			log.error("更新用户失败！");
			return "operateFailed";
		}
		return "operateSuccess";
	}

	/**
	 * 跳转到新增用户页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addUserLink")
	public String addUserLink(HttpServletRequest request,
			HttpServletResponse response) {
		return "addUser";
	}

	/**
	 * 新增用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addUser")
	public String addUser(HttpServletRequest request,
			HttpServletResponse response) {
		userName = request.getParameter("userName");
		String pwd = request.getParameter("password");
		String email = request.getParameter("email");
		User user = new User();
		user.setUserName(userName);
		user.setPassword(pwd);
		user.setEmail(email);
		user.setRegDate(new Date());
		
		int result = this.getUserService().addUser(user);
		if (1 == result) {
			log.info("add user success!");
		} else {
			log.info("add user failed!");
		}
		return "operateSuccess";
	}

	/**
	 * 跳转到删除用户-输入页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("deleteUserLink")
	public String deleteUserLink(HttpServletRequest request,
			HttpServletResponse response) {
		return "deleteUserInput";
	}
	
	/**
	 * 跳转到删除用户-反显信息页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("deleteUserInput")
	public String deleteUserInput(HttpServletRequest request,
			HttpServletResponse response) {
		userInfo(request, response);
		return "deleteUser";
	}

	/**
	 * 跳转到更新用户页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("deleteUser")
	public String deleteUser(HttpServletRequest request,
			HttpServletResponse response) {
		userName = request.getParameter("userName");
		User user = new User();
		user.setUserName(userName);
		int result = this.getUserService().deleteUser(user);
		if (1 == result) {
			log.info("delete user success！");
		} else {
			log.error("delete user failed！");
			return "operateFailed";
		}
		return "operateSuccess";
	}

	/**
	 * 跳转到查询用户页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("queryUsersLink")
	public String queryUserLink(HttpServletRequest request,
			HttpServletResponse response) {
		return "queryUsers";
	}

	/**
	 * 查询用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("queryUsers")
	public String queryUsers(HttpServletRequest request,
			HttpServletResponse response) {
		userName = request.getParameter("userName");
		User param = new User();
		param.setUserName(userName);
		List<User> users= this.getUserService().selectUsers(param);
		if (null != users && users.size() != 0) {
			request.setAttribute("users", users);
			return "queryUsersList";
		} else {
			request.setAttribute("errMsg", "未查询到满足条件的用户，请更改查询条件后再试！");
			return "queryUsers";
		}
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
