package com.example.itsmypet.Model;

public class RModel {
    String img, name, address,pCode,country,details;

    public RModel(String img, String name, String address, String pCode, String country, String details) {
        this.img = img;
        this.name = name;
        this.address = address;
        this.pCode = pCode;
        this.country = country;
        this.details = details;
    }

    public RModel() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
