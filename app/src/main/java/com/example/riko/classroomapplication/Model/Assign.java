package com.example.riko.classroomapplication.Model;

public class Assign {

    private String assignname;
    private String subjectID;

    public Assign() {
    }

    public Assign(String assignname, String subjectID) {
        this.assignname = assignname;
        this.subjectID = subjectID;
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
}
