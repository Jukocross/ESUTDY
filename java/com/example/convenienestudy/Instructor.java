package com.example.convenienestudy;

import java.util.ArrayList;

public class Instructor extends Users {

    private static int idCounter;
    private String instructorId;
    private ArrayList<Quiz> lstOfQuiz;

    public Instructor(String name, String email, String schoolId, String userId) {
        super(name, email, schoolId, userId);
        this.instructorId = String.valueOf(idCounter);
        idCounter++;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public boolean addQuiz(Quiz q){
        boolean added = false;
        if (!lstOfQuiz.contains(q)){
            lstOfQuiz.add(q);
            added = true;
        }
        return added;
    }

    public boolean removeQuiz(Quiz q){
        boolean removed = false;
        if (lstOfQuiz.contains(q)){
            lstOfQuiz.remove(q);
            removed = true;
        }
        return removed;
    }

}
