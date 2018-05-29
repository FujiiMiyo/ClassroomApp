package com.example.riko.classroomapplication.Model;

public class Answer {
    private String Username;
    private String Assignname;
    private String SubjectID;
    private int Score;

    public Answer(){
    }

    public Answer(String username,String assignname,String subjectID){
        this.Username = username;
        this.Assignname = assignname;
        this.SubjectID = subjectID;
    }

    public int getScore() {
        return Score;
    }

    public String getAssignname() {
        return Assignname;
    }

    public String getSubjectID() {
        return SubjectID;
    }

    public String getUsername() {
        return Username;
    }

    public void setScore(int score) {
        Score = score;
    }

    public void setAssignname(String assignname) {
        Assignname = assignname;
    }

    public void setSubjectID(String subjectID) {
        SubjectID = subjectID;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
