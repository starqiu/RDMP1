/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * Test.java
 * 2014-3-1
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 实现功能：
 * <p>
 * date author email notes<br />
 * -------- --------------------------- ---------------<br />
 * 2014-3-1 邱星 starqiu@mail.ustc.edu.cn 新建类<br />
 * </p>
 * 
 */

@Controller
public class TestAction {
	private String message;

	public TestAction() {
		super();
	}

	@RequestMapping(value = "/test.do")
	public ModelAndView helloWorld(HttpServletRequest request,HttpServletResponse response) {
		
		System.out.println(message);//bean 注入传参
		System.out.println(request.getParameter("fileName"));//页面参数传递
		System.out.println(request.getSession().getServletContext().getRealPath("attach"));//页面参数传递
		return new ModelAndView("hello", "message", message);
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
