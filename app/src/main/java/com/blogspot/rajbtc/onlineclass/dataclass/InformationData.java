package com.blogspot.rajbtc.onlineclass.dataclass;

import com.google.firebase.database.DatabaseReference;

public class InformationData {
    private DatabaseReference reference;
    private String name, roll, blood, phone, mail;

    public InformationData() {
    }

    public InformationData(DatabaseReference reference, String name, String roll, String blood, String phone, String mail) {
        this.reference = reference;
        this.name = name;
        this.roll = roll;
        this.blood = blood;
        this.phone = phone;
        this.mail = mail;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }

    public String getBlood() {
        return blood;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
