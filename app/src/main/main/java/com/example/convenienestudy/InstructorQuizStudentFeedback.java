package com.example.convenienestudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class InstructorQuizStudentFeedback extends AppCompatActivity {

    //TODO List down the number of students
    //Get the name and score of the users and show them in the a recyclerview
    private RecyclerView studentRV;
    private HashMap<String, String> listOfStudents = new HashMap<String,String>(); //Key = userId, Value = studentId
    private ArrayList<Student> students;
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_quiz_student_feedback);

        Intent getIntent = getIntent();
        listOfStudents = (HashMap<String,String>) getIntent.getSerializableExtra("listOfStudent");
//
//        for (String userId : listOfStudents.keySet()){
//            usersRef.child(userId).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    students.add(snapshot.getValue(Student.class));
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            })
//        }
//
//        studentRV = (RecyclerView) findViewById(R.id.instructorStudentsFeedback_recyclerview_id);
//        studentRV.setLayoutManager(new LinearLayoutManager(this));
//        studentRV.setAdapter(studentAdapter);


    }
}
