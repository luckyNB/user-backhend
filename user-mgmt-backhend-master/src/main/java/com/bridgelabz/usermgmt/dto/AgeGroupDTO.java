package com.bridgelabz.usermgmt.dto;

import lombok.Data;

@Data
public class AgeGroupDTO {

	private Integer group1;
	private Integer group2;
	private Integer group3;
	private Integer group4;
	private Integer group5;
	private Integer group6;
	private Integer group7;
	public Integer getGroup1() {
		return group1;
	}
	public void setGroup1(Integer group1) {
		this.group1 = group1;
	}
	public Integer getGroup2() {
		return group2;
	}
	public void setGroup2(Integer group2) {
		this.group2 = group2;
	}
	public Integer getGroup3() {
		return group3;
	}
	public void setGroup3(Integer group3) {
		this.group3 = group3;
	}
	public Integer getGroup4() {
		return group4;
	}
	public void setGroup4(Integer group4) {
		this.group4 = group4;
	}
	public Integer getGroup5() {
		return group5;
	}
	public void setGroup5(Integer group5) {
		this.group5 = group5;
	}
	public Integer getGroup6() {
		return group6;
	}
	public void setGroup6(Integer group6) {
		this.group6 = group6;
	}
	public Integer getGroup7() {
		return group7;
	}
	public void setGroup7(Integer group7) {
		this.group7 = group7;
	}
	@Override
	public String toString() {
		return "AgeGroupDTO [group1=" + group1 + ", group2=" + group2 + ", group3=" + group3 + ", group4=" + group4
				+ ", group5=" + group5 + ", group6=" + group6 + ", group7=" + group7 + "]";
	}
	

	
	
}
