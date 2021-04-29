package com.blogspot.rajbtc.onlineclass.dataclass;

public class MyUserData {
    public String name, email, school, userType,passForUser;

    public MyUserData(){

    }

    public MyUserData(String name, String email, String school, String userType,String passForUser) {
        this.name = name;
        this.email=email;
        this.school = school;
        this.userType = userType;
        this.passForUser=passForUser;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSchool() {
        return school;
    }

    public String getUserType() {
        return userType;
    }

    public String getPassForUser() {
        return passForUser;
    }
}
