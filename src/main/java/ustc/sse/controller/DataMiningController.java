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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ustc.sse.rjava.RJavaInterface;

/**
 * 实现功能： 数据挖掘控制器
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-3-3	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
@Controller
public class DataMiningController extends RJavaInterface{
	private Log log = LogFactory.getLog(DataMiningController.class);
	
	public DataMiningController(){
		super();
	}
	
	@RequestMapping("mapred.do")
	public ModelAndView mapred(HttpServletRequest request,HttpServletResponse response){
		callRJava();
		String cmd = "source('../r/WordCount.R')";
        String rv = re.eval(cmd).asString();
        log.info("the result of source WordCount.R is:"+rv);
        
		return new ModelAndView("result","mapRedRst",rv);
	}

}

