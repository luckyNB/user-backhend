package com.bridgelabz.usermgmt.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "login_date_history")
public class LoginDateHistory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8003304822902469988L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "login_date_history_id")
	private Long login_date_history_id;
	
	@Column(name = "login_datetime_history")
	private LocalDateTime login_datetime_history;

	@Column(name = "user_id")
	private Long user_id;

	public Long getLogin_date_history_id() {
		return login_date_history_id;
	}

	public void setLogin_date_history_id(Long login_date_history_id) {
		this.login_date_history_id = login_date_history_id;
	}

	public LocalDateTime getLogin_datetime_history() {
		return login_datetime_history;
	}

	public void setLogin_datetime_history(LocalDateTime login_datetime_history) {
		this.login_datetime_history = login_datetime_history;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	
}
