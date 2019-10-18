package com.bridgelabz.usermgmt.util;

import org.springframework.stereotype.Component;

import com.bridgelabz.usermgmt.model.User;

@Component
public class Response {
	
	private Integer statusCode;
	private String statusMessage;
	
	private User user;
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	

}
