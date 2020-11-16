package com.example.convenienestudy;

import java.util.ArrayList;
import java.util.UUID;

public class Student extends Users {

    private static int idCounter = 0;
    private int studentId;
    private ArrayList<Quiz> quizArrayList;

    public Student(String name, String email, int schoolID , String userId) {
        super(name, email, schoolID, userId);
        this.studentId = idCounter;
        idCounter++;
    }

    public int getStudentID() {
        return studentId;
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
