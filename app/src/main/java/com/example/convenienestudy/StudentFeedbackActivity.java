package com.example.convenienestudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StudentFeedbackActivity extends AppCompatActivity {

    private Assignment assignment;
    private TextView feedbackText;
    private Button backHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_feedback);

        Intent getIntent = getIntent();
        assignment = getIntent.getExtras().getParcelable("assignmentObject");

        String feedback = assignment.getFeedback();

        feedbackText = (TextView) findViewById(R.id.student_feedback);
        backHomeButton = (Button) findViewById(R.id.feedback_backHome_button);

        feedbackText.setText(feedback);

        backHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentFeedbackActivity.this, StudentMainActivity.class));
            }
        });

    }
}