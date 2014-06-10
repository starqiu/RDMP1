/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * Classifer.java
 * 2014-5-31
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.model;

import java.math.BigDecimal;

/**
 * 实现功能： 分类器
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-5-31	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
public class Classifer {
	
	/** 分类器名称*/
	private String name;
	/** 可能性*/
	private BigDecimal probability;
	
	public Classifer() {
		super();
	}
	
	public Classifer(String name, BigDecimal probability) {
		super();
		this.name = name;
		this.probability = probability;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getProbability() {
		return probability;
	}
	public void setProbability(BigDecimal probability) {
		this.probability = probability;
	}

}

