package com.example.riko.classroomapplication.Model;

public class Subject {

    private String subjectID;
    private String subjectname;

    public Subject() {
    }

    public Subject(String subjectID, String subjectname) {
        this.subjectID = subjectID;
        this.subjectname = subjectname;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

}