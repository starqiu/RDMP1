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
public class DataMiningController {
	private Log log = LogFactory.getLog(DataMiningController.class);
	
	public DataMiningController(){
		super();
	}
	
	@RequestMapping("dataMining")
	public String dataMining(HttpServletRequest request,HttpServletResponse response){
		if (!RJavaInterface.getRengine().waitForR()) {
        	log.error("Can not load R!");
        }
		
		//数据挖掘
		String cmd = "source('/home/starqiu/workspace/RDMP1/src/main/java/ustc/sse/r/tbmr.R',echo=TRUE)";
        String rv = "";
        try {
			rv = RJavaInterface.getRengine().eval(cmd).asString();
			log.info("data mining succeed!");
			log.info("the result of source tbmr.R is:"+rv);
		} catch (Exception e) {
			log.error("data mining failed!");
		}
        
        //画图
        cmd = "source('/home/starqiu/workspace/RDMP1/src/main/java/ustc/sse/r/draw.R',echo=TRUE)";
        try {
			rv = RJavaInterface.getRengine().eval(cmd).asString();
			log.info("data visual succeed!");
			log.info("the result of source draw.R is:"+rv);
		} catch (Exception e) {
			log.error("data visual failed!");
		}
        
        RJavaInterface.getRengine().end();
		return "taskDetail";
	}

}

