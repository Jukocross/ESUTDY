package com.example.convenienestudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentQuizSummaryActivity extends AppCompatActivity {

    //TODO UPDATE THE ASSIGNMENT OF THE USER TO CLOUD

    private Quiz quiz;
    private Assignment assignment;
    private Button endOfQuizButton;
    private TextView showScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz_summary);

        Intent getIntent = getIntent();
        quiz = getIntent.getExtras().getParcelable("quizObject");
        assignment = getIntent.getExtras().getParcelable("assignmentObject");

        assignment.setCompleted(true);

        showScore = (TextView) findViewById(R.id.quiz_summary_score);
        endOfQuizButton = (Button) findViewById(R.id.quiz_summary_button);

        showScore.setText(String.valueOf(assignment.getScore()));

    }
}