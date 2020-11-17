package com.example.convenienestudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentQuizActivity extends AppCompatActivity {

    private TextView quizTitle, quizScore, quizDescription;
    private Quiz quiz;
    private Assignment assignment;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz);

        quizTitle = (TextView) findViewById(R.id.quiz_start_title);
        quizScore = (TextView) findViewById(R.id.quiz_start_score);
        quizDescription = (TextView) findViewById(R.id.quiz_start_description);
        startButton = (Button) findViewById(R.id.start_quiz_button);

        Intent getIntent = getIntent();
        quiz = getIntent.getExtras().getParcelable("quizObject");
        assignment = getIntent.getExtras().getParcelable("assignmentObject");

        if(assignment.isCompleted()){
            startButton.setText("Check Out Your Review Here");
            startButton.setOnClickListener(feedbackListener);
        }
        else{
            startButton.setOnClickListener(startQuestionsListener);
        }
    }

    View.OnClickListener startQuestionsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StudentQuizActivity.this, StudentAnswerQuizActivity.class);
            intent.putExtra("quizObject", quiz);
            intent.putExtra("assignmentObject", assignment);
            startActivity(intent);
        }
    };

    View.OnClickListener feedbackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StudentQuizActivity.this, StudentFeedbackActivity.class);
            intent.putExtra("assignmentObject", assignment);
            startActivity(intent);
        }
    };

}