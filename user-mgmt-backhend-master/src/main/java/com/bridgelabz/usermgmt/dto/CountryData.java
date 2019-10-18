package com.bridgelabz.usermgmt.dto;

import lombok.Data;

@Data
public class CountryData {

	private Long india;
	private Long pakistan;
	private Long bangladesh;
	private Long others;
	public Long getIndia() {
		return india;
	}
	public void setIndia(Long india) {
		this.india = india;
	}
	public Long getPakistan() {
		return pakistan;
	}
	public void setPakistan(Long pakistan) {
		this.pakistan = pakistan;
	}
	public Long getBangladesh() {
		return bangladesh;
	}
	public void setBangladesh(Long bangladesh) {
		this.bangladesh = bangladesh;
	}
	public Long getOthers() {
		return others;
	}
	public void setOthers(Long others) {
		this.others = others;
	}
	
	
	
}
