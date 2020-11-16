package com.example.convenienestudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InstructorAddQuiz extends AppCompatActivity {

    private Button createQuiz;
    private TextInputLayout quizTitle, quizDescription;
    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("lstOfQuiz");
    private String title, description;
    private String noOfQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_add_quiz);

        createQuiz = (Button) findViewById(R.id.createQuizButton);
        quizTitle = (TextInputLayout) findViewById(R.id.createQuizTitle);
        quizDescription = (TextInputLayout) findViewById(R.id.createQuizDescription);

        Intent getIntent = getIntent();
        noOfQuiz = Integer.toString(getIntent.getIntExtra("numberOfQuiz", 0));

        createQuiz.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                title = quizTitle.getEditText().getText().toString();
                description = quizDescription.getEditText().getText().toString();
                quizRef.addListenerForSingleValueEvent(checkForChild);
            }
        });
    }

    ValueEventListener checkForChild = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (!snapshot.hasChild(noOfQuiz)){
                quizRef.child(noOfQuiz).setValue(new Quiz(title, description, Integer.parseInt(noOfQuiz)));
                startActivity(new Intent(InstructorAddQuiz.this, InstructorMainActivity.class));
            }
            else {
                Toast.makeText(InstructorAddQuiz.this, "Quiz exist", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}