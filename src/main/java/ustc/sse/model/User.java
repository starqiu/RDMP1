/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * User.java
 * 2014-3-17
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.model;

import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 实现功能： 用户实体类
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-3-17	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
/**
 * @author 邱星
 */
public class User {
	/** 用户名*/
	@Size(min=3,max=20,message="user.length.required")
	@Pattern(regexp="^{[a-zA-Z0-9]+_?}+[a-zA-Z0-9]+$",message="user.ptn.required")
	private String userName ;
	/** 密码*/
	@Size(min=6,max=20,message="pwd.length.required")
	@Pattern(regexp="^[a-zA-Z0-9!@#$%*.]+$",message="pwd.ptn.required")
	private String password ;
	/** 邮箱*/
	@Pattern(regexp="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}")
	private String email ;
	/** 注册日期*/
	private Date regDate ;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}

