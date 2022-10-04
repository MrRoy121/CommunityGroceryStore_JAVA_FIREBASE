package com.example.communitygrocerystoreapp.utils;

public class UserModel {
    String email, fname, lname, pass, phone, hno, hname, city, uid;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHno() {
        return hno;
    }

    public void setHno(String hno) {
        this.hno = hno;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserModel(String email, String fname, String lname, String pass, String phone, String hno, String hname, String city, String uid) {
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.pass = pass;
        this.phone = phone;
        this.hno = hno;
        this.hname = hname;
        this.city = city;
        this.uid = uid;
    }
}
