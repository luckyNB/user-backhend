package com.bridgelabz.usermgmt.model;




import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
public class Permissions {


	@Column(name = "add")
	public boolean add;

	@Column(name = "delete")
	public boolean delete;

	@Column(name = "update")
	public boolean update;

	@Column(name = "read")
	public boolean read;

	public Permissions(boolean add, boolean delete, boolean update, boolean read) {
		super();
		this.add = add;
		this.delete = delete;
		this.update = update;
		this.read = read;
	}

	public Permissions() {
		
	}
	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}



	
}
