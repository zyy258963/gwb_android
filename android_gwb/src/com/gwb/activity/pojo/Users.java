package com.gwb.activity.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class Users implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userId;
	private String userName;
	private String password;
	private String telephone;
	private String macAddress;
	private int isAvailable;

	public Users() {
		// TODO Auto-generated constructor stub
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public int getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(int isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	@Override
	public String toString() {
		return "Users [userId=" + userId + ", userName=" + userName
				+ ", password=" + password + ", telephone=" + telephone
				+ ", macAddress=" + macAddress + ", isAvailable=" + isAvailable
				+ "]";
	}
}
