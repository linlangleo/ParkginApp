package com.example.leo.parkingapp.entity;

import java.util.Date;

/**
 * Carowneruser entity. @author MyEclipse Persistence Tools
 */
public class CarOwnerUser implements java.io.Serializable {
	private Integer id;
	
	private String userName;
	
	private String password;
	
	private String phone;
	
	private Integer boundPlateNumberCount;
	
	private String nickName;
	
	private String sex;
	
	private Date borndate;
	
	private Date registrationTime;
	
	private String realName;
	
	private String identityCard;

	private Date creationDate;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getBoundPlateNumberCount() {
		return boundPlateNumberCount;
	}

	public void setBoundPlateNumberCount(Integer boundPlateNumberCount) {
		this.boundPlateNumberCount = boundPlateNumberCount;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBorndate() {
		return borndate;
	}

	public void setBorndate(Date borndate) {
		this.borndate = borndate;
	}

	public Date getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(Date registrationTime) {
		this.registrationTime = registrationTime;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	
	
}