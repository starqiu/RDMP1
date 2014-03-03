/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * InputController.java
 * 2014-3-3
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 实现功能： 输入控制器
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-3-3	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
@Controller
public class InputController {
	
	public InputController(){
		super();
	}
	
	@RequestMapping("/selectFile.do")
	public ModelAndView selectFile(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("inputOthers");
	}

}

