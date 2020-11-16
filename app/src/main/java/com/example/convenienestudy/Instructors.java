package com.example.convenienestudy;

import java.util.ArrayList;

public class Instructors extends Users {

    private static int idCounter;
    private int instructorsId;
    private ArrayList<Quiz> quizArrayList;

    public Instructors(String name, String email, int schoolId, String userId) {
        super(name, email, schoolId, userId);
        this.instructorsId = idCounter;
        idCounter++;
    }

    public int getInstructorsId() {
        return instructorsId;
    }

    public boolean addQuiz(Quiz q){
        boolean added = false;
        if (!quizArrayList.contains(q)){
            quizArrayList.add(q);
            added = true;
        }
        return added;
    }

    public boolean removeQuiz(Quiz q){
        boolean removed = false;
        if (quizArrayList.contains(q)){
            quizArrayList.remove(q);
            removed = true;
        }
        return removed;
    }

}
