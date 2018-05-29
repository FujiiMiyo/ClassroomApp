package com.example.riko.classroomapplication.Model;

public class Assign {

    private String assignname;
    private String subjectID;
    private int assignnumber;

    public Assign() {
    }

    public Assign(String assignname, String subjectID,int assignnumber) {
        this.assignname = assignname;
        this.subjectID = subjectID;
        this.assignnumber = assignnumber;
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

    public int getAssignnumber() {
        return assignnumber;
    }

    public void setAssignnumber(int assignnumber) {
        this.assignnumber = assignnumber;
    }
}
