package com.nubar.jime.Classes;

public class userCareerinfo {
    int careername;
    String careerlocation;
    String total_crystals;
    String fee;

    public userCareerinfo() {
    }

    public userCareerinfo(int careername, String careerlocation, String total_crystals,String fee) {
        this.careername = careername;
        this.careerlocation = careerlocation;
        this.total_crystals = total_crystals;
        this.fee=fee;
    }

    public int getCareername() {
        return careername;
    }

    public String getCareerlocation() {
        return careerlocation;
    }

    public String getTotal_crystals() {
        return total_crystals;
    }

    public String getFee() {
        return fee;
    }
}
