package com.example.itsmypet.Model;

public class PetModel {
    String name, address , pincode, country , img;

    public PetModel(String name, String address, String pincode, String country, String img) {
        this.name = name;
        this.address = address;
        this.pincode = pincode;
        this.country = country;
        this.img = img;

    }

    public PetModel() {
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


}
