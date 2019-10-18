package com.bridgelabz.usermgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.usermgmt.model.LoginDateHistory;

public interface LoginRepository extends JpaRepository<LoginDateHistory, Long> {

}
