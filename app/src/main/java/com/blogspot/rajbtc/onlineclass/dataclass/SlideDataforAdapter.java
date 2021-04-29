package com.blogspot.rajbtc.onlineclass.dataclass;

import com.google.firebase.database.DatabaseReference;

public class SlideDataforAdapter {
    private DatabaseReference reference;
    private String slideName,subjectName,teacherName,link,time,date;

    public SlideDataforAdapter(){}


    public SlideDataforAdapter(DatabaseReference reference, String slideName, String subjectName, String teacherName, String link, String time, String date) {
        this.reference = reference;
        this.slideName = slideName;
        this.subjectName = subjectName;
        this.teacherName = teacherName;
        this.link = link;
        this.time = time;
        this.date = date;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public void setSlideName(String slideName) {
        this.slideName = slideName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public String getSlideName() {
        return slideName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getLink() {
        return link;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

}
