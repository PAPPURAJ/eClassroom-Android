package com.blogspot.rajbtc.onlineclass.dataclass;

import com.google.firebase.database.DatabaseReference;

public class NoticeData {
    private DatabaseReference reference;
    private String notice,time,date;

    public NoticeData(){}

    public NoticeData(DatabaseReference reference,String notice, String time, String date) {
        this.reference=reference;
        this.notice = notice;
        this.time = time;
        this.date = date;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public String getNotice() {
        return notice;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
