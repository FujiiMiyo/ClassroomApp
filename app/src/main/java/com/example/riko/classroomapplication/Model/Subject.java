package com.example.riko.classroomapplication.Model;

public class Subject {

    private String subjectID;
    private String subjectname;
    private String username;
    private String time;

    public Subject() {
    }

    public Subject(String subjectID, String subjectname, String username, String time) {
        this.subjectID = subjectID;
        this.subjectname = subjectname;
        this.username = username;
        this.time = time;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}