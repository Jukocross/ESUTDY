package com.example.convenienestudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class StudentMainActivity extends AppCompatActivity {


    //TODO STUDENT PAGES WITH COMPLETED ASSIGNMENT AND NON-COMPLETED ASSIGNMENT
    //TODO ABLE TO DO NON-COMPLETE ASSIGNMENT AND UPDATE ACCORDINGLY
    //TODO ABLE TO CHECK THE FEEDBACK IF THE ASSIGNMENT IS DONE

    private ArrayList<Quiz> lstQuiz;
    private RecyclerView quizRV;
    private FloatingActionButton btnFloating;
    private RecyclerViewAdapterInstructorQuiz quizAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
    }
}