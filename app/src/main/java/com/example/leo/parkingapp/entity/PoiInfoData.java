package com.example.leo.parkingapp.entity;

/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.entity
 * 文件名：    PoiInfoData
 * 创建者：    leo
 * 创建时间：   2018/3/1 19:26
 * 描述： Poi信息
 */
public class PoiInfoData {
    //poi名称
    private String name;

    //poi城市
    private String city;

    //poi地址
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
