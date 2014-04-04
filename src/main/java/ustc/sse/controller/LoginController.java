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

import ustc.sse.jdbc.User;
import ustc.sse.service.UserService;

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
		List<User> list=this.userService.findAll();
		for(User u:list){
			System.out.println(u.getName()+","+u.getEmail());
		}
		
		return "index";
	}
	
}

