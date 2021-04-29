package com.blogspot.rajbtc.onlineclass.dataclass;

import com.google.firebase.database.DatabaseReference;

public class ClassData {
    private DatabaseReference docID;
    private  String className,day,teacher,startTime,endTime,lastUpdate,link;

    public ClassData() {
    }

    public ClassData(DatabaseReference docID, String className, String day, String teacher, String startTime, String endTime, String lastUpdate, String link) {
        this.docID=docID;
        this.className = className;
        this.day=day;
        this.teacher = teacher;
        this.startTime = startTime;
        this.lastUpdate = lastUpdate;
        this.endTime = endTime;
        this.link = link;
    }

    public DatabaseReference getDocID() {
        return docID;
    }

    public String getClassName() {
        return className;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDate() {
        return lastUpdate;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getLink() {
        return link;
    }


    public void setDocID(DatabaseReference docID) {
        this.docID = docID;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
