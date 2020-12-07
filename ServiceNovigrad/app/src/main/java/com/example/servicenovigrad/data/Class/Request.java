package com.example.servicenovigrad.data.Class;

import java.util.List;

public class Request {
    Customer customer;
    List<String> imageList;
    String licenseType, serviceId, time, date, status,id,token;


    public Request() {
    }

    public Request(String id,Customer customer, List<String> imageList, String licenseType, String serviceId, String time, String date, String status,String token) {
        this.id = id;
        this.customer = customer;
        this.imageList = imageList;
        this.licenseType = licenseType;
        this.status = status;
        this.serviceId = serviceId;
        this.time = time;
        this.date = date;
        this.token = token;
    }

    public Request(Customer customer, List<String> imageList) {
        this.customer = customer;
        this.imageList = imageList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
