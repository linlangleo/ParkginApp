package com.example.leo.parkingapp.entity;

import java.util.Date;

/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.entity
 * 文件名：    ParkingInfo
 * 创建者：    leo
 * 创建时间：   2018/3/3 10:50
 * 描述： TODO
 */
public class ParkingInfo {
    private Integer id;

    private String parkingName;

    private String parkingAddress;

    private Integer charge;

    private Integer parkingSpaceCount;

    private String street;

    private String ifOpenSharingPlatform;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getParkingAddress() {
        return parkingAddress;
    }

    public void setParkingAddress(String parkingAddress) {
        this.parkingAddress = parkingAddress;
    }

    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public Integer getParkingSpaceCount() {
        return parkingSpaceCount;
    }

    public void setParkingSpaceCount(Integer parkingSpaceCount) {
        this.parkingSpaceCount = parkingSpaceCount;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getIfOpenSharingPlatform() {
        return ifOpenSharingPlatform;
    }

    public void setIfOpenSharingPlatform(String ifOpenSharingPlatform) {
        this.ifOpenSharingPlatform = ifOpenSharingPlatform;
    }
}
