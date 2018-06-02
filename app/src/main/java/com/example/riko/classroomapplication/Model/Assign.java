package com.example.riko.classroomapplication.Model;

public class Assign {

    private String assignname;
    private String subjectID;
    private String time;

    public Assign() {
    }

    public Assign(String assignname, String subjectID, String time) {
        this.assignname = assignname;
        this.subjectID = subjectID;
        this.time = time;
    }

    public String getAssignname() {
        return assignname;
    }

    public void setAssignname(String assignname) {
        this.assignname = assignname;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
