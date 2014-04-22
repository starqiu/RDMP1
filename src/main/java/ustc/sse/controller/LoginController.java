/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * Login.java
 * 2014-3-3
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.controller;

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
 * 实现功能： 登录、注册及注销
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-3-3	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
@Controller
public class LoginController {
	private final static Log log = LogFactory.getLog(LoginController.class);
	private String userName;
	@Resource
	private UserService userService;
	

	public LoginController() {
		super();
	}

	/**
	 * @param request
	 * @param response
	 * @return 首页
	 */
	@RequestMapping({"/","/index"})
	public String index(HttpServletRequest request,HttpServletResponse response){
		log.info("go to index page!");
		List<User> users = this.getUserService().selectAllUsers();
		
		return "index";
	}
	
	/**
	 * 跳转到登录界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("login")
	public String login(HttpServletRequest request,HttpServletResponse response){
		return "login";
	}
	
	/**
	 * 用户登录，跳转到欢迎界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("welcome")
	public String welcome(HttpServletRequest request,HttpServletResponse response){
		userName = request.getParameter("userName");
		String pwd = request.getParameter("password");
		User user = this.getUserService().selectUserByName(userName);
		if (null != user && pwd != null && pwd.equals(user.getPassword())) {
			request.getSession().setAttribute("userName", user.getUserName());
			log.info("login success!");
			return "index";
		}else {
			request.setAttribute("loginFailed", "1");
			log.error("login failed!");
			return "login";
		}
	}
	
	public UserService getUserService() {
		return userService;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}

