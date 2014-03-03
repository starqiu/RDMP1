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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 实现功能： 登录
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-3-3	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
@Controller
public class LoginController {
	private String userName;
	
	public LoginController() {
		super();
	}
	
	/**
	 * @param request
	 * @param response
	 * @return 登录页面
	 */
	@RequestMapping("/login.do")
	public ModelAndView login(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("login");
	}
	
	/**
	 * @param request
	 * @param response
	 * @return 欢迎页面
	 */
	@RequestMapping("/welcome.do")
	public ModelAndView welcome(HttpServletRequest request,HttpServletResponse response){
		//获取Form表单中的用户名/邮箱
		userName = request.getParameter("userName");
		//将用户名/邮箱存放到Session中以便会话时能随时用于显示 页面中取该值的方式为${sessionScope.userName}
		request.getSession().setAttribute("userName", userName);
		return new ModelAndView("welcome");
	}
	
	/**
	 * @param request
	 * @param response
	 * @return about页面
	 */
	@RequestMapping("/about.do")
	public ModelAndView about(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("about");
	}
	
	/**
	 * @param request
	 * @param response
	 * @return 联系页面
	 */
	@RequestMapping("/contact.do")
	public ModelAndView contact(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("contact");
	}
	
	/**
	 * @param request
	 * @param response
	 * @return 首页
	 */
	@RequestMapping("/index.do")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("index");
	}

}

