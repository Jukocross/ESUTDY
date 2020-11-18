package com.example.convenienestudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StudentQuizSummaryActivity extends AppCompatActivity {

    private Quiz quiz;
    private Assignment assignment;
    private Button endOfQuizButton;
    private TextView showScore;
    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference assignmentRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("listOfAssignment");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz_summary);

        Intent getIntent = getIntent();

        assignment = getIntent.getExtras().getParcelable("assignmentObject");

        assignment.setCompleted(true);

        showScore = (TextView) findViewById(R.id.quiz_summary_score);
        endOfQuizButton = (Button) findViewById(R.id.quiz_summary_button);

        showScore.setText(String.valueOf(assignment.getScore()));

        endOfQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignmentRef.child(assignment.getQuizNumber()).child("completed").setValue(true);
                startActivity(new Intent(StudentQuizSummaryActivity.this, StudentMainActivity.class));
            }
        });

    }
}