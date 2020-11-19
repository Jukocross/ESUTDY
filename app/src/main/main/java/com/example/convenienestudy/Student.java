package com.example.convenienestudy;

import java.util.ArrayList;
import java.util.UUID;

public class Student extends Users {

    private static int idCounter = 0;
    private String studentId;
    private ArrayList<Assignment> listOfAssignment;

    public Student(String name, String email, String schoolID , String userId) {
        super(name, email, schoolID, userId);
        this.studentId = String.valueOf(idCounter);
        idCounter++;
    }

    public String getStudentId() {
        return studentId;
    }

    public boolean addQuiz(Assignment q){
        boolean added = false;
        if (!listOfAssignment.contains(q)){
            listOfAssignment.add(q);
            added = true;
        }
        return added;
    }

    public boolean removeQuiz(Assignment q){
        boolean removed = false;
        if (listOfAssignment.contains(q)){
            listOfAssignment.remove(q);
            removed = true;
        }
        return removed;
    }


}
