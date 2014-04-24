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

import java.util.Date;

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
	 * 跳转到首页
	 * @param request
	 * @param response
	 * @return 首页
	 */
	@RequestMapping({"/","/index"})
	public String index(HttpServletRequest request,HttpServletResponse response){
		log.info("go to index page!");
		return "index";
	}
	
	/**
	 * 跳转到登录界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("loginLink")
	public String loginLink(HttpServletRequest request,HttpServletResponse response){
		return "login";
	}
	/**
	 * 跳转到关于我们界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("about")
	public String about(HttpServletRequest request,HttpServletResponse response){
		return "about";
	}
	/**
	 * 跳转到联系我们界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("contact")
	public String contact(HttpServletRequest request,HttpServletResponse response){
		return "contact";
	}
	
	/**
	 * 用户登录，跳转到欢迎界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("login")
	public String login(HttpServletRequest request,HttpServletResponse response){
		userName = request.getParameter("userName");
		String pwd = request.getParameter("password");
		User param = new User();
		param.setUserName(userName);
		User user = this.getUserService().selectUserByName(param);
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
	
	/**
	 * 用户注册界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("registerLink")
	public String registerLink(HttpServletRequest request,HttpServletResponse response){
		return "register";
	}
	
	/**
	 * 用户注册，成功后跳转到欢迎界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("register")
	public String register(HttpServletRequest request,HttpServletResponse response){
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
			log.info("register success!");
			login(request, response);
		} else {
			log.info("register failed!");
		}
		return "index";
	}
	
	/**
	 * 用户注销，跳转到首页界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("logout")
	public String logout(HttpServletRequest request,HttpServletResponse response){
		userName = (String) request.getSession().getAttribute("userName");
		if (null != userName) {
			request.getSession().removeAttribute("userName");
			log.info("logout success!");
		}
		return "index";
	}
	
	public UserService getUserService() {
		return userService;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}

