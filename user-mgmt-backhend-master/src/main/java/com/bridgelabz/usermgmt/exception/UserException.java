package com.bridgelabz.usermgmt.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class UserException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1549395279401147741L;
	int code;
	String msg;
	 public UserException(String msg)
	 {
		super(msg);
	 }
	 public UserException(int code, String msg)
	 {
		 super(msg);
		 this.code =code;
	 }
}
