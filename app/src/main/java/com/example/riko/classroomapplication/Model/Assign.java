package com.example.riko.classroomapplication.Model;

public class Assign {

    String subjectID;
    String assignname;

    public Assign() {
    }

    public Assign(String subjectID, String assignname) {
        this.subjectID = subjectID;
        this.assignname = assignname;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getAssignname() {
        return assignname;
    }

    public void setAssignname(String assignname) {
        this.assignname = assignname;
    }
}
