package com.blogspot.rajbtc.onlineclass.dataclass;

public class RealtimeDataClass {
    private String ClassName,Teacher,StartTime,LastUpdate,Duration,Link,Day;

    public RealtimeDataClass(String className, String teacher, String startTime, String lastUpdate, String duration, String link, String day) {
        ClassName = className;
        Teacher = teacher;
        StartTime = startTime;
        LastUpdate = lastUpdate;
        Duration = duration;
        Link = link;
        Day = day;
    }

    public String getClassName() {
        return ClassName;
    }

    public String getTeacher() {
        return Teacher;
    }

    public String getStartTime() {
        return StartTime;
    }

    public String getLastUpdate() {
        return LastUpdate;
    }

    public String getDuration() {
        return Duration;
    }

    public String getLink() {
        return Link;
    }

    public String getDay() {
        return Day;
    }

}
