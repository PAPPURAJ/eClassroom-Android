package com.blogspot.rajbtc.onlineclass.dataclass;

import com.google.firebase.database.DatabaseReference;

public class VideoDataclass {
    private DatabaseReference reference;
    private String videoName,subjectName,teacherName,link,date,time;

    public VideoDataclass(){}

    public VideoDataclass(DatabaseReference reference, String videoName, String subjectName, String teacherName, String link, String date, String time) {
        this.reference = reference;
        this.videoName = videoName;
        this.subjectName = subjectName;
        this.teacherName=teacherName;
        this.link = link;
        this.date = date;
        this.time = time;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public String getVideoName() {
        return videoName;
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

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
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

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
