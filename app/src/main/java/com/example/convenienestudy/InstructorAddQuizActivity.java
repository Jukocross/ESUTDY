package com.example.convenienestudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class InstructorAddQuizActivity extends AppCompatActivity {

    //TODO MAKE THE QUIZ ID UNIQUE BY THE ID COUNTER BIGGER THEN THE ARRAY

    private Button createQuiz;
    private TextInputLayout quizTitle, quizDescription;
    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
    private DatabaseReference quizRef = userRef.child("lstOfQuiz");
    private String title, description, noOfQuiz;
    private int instructorId;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_add_quiz);

        createQuiz = (Button) findViewById(R.id.createQuizButton);
        quizTitle = (TextInputLayout) findViewById(R.id.createQuizTitle);
        quizDescription = (TextInputLayout) findViewById(R.id.createQuizDescription);

        mPreferences = getSharedPreferences(LoginActivity.sharedPreFile, MODE_PRIVATE);
        instructorId = mPreferences.getInt(LoginActivity.instructorIdKey, 0);

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
            boolean existed = false;
            for (DataSnapshot ds : snapshot.getChildren()){
                String tempString = ds.child("title").getValue(String.class);
                Log.d("TESTING TITLE", "Title is " + title + " And the child is " + tempString);
                if (title.equalsIgnoreCase(tempString)){
                    existed = true;
                }
            }
            if (!existed){
                quizRef.child(noOfQuiz).setValue(new Quiz(title, description, Integer.parseInt(noOfQuiz), instructorId));
                startActivity(new Intent(InstructorAddQuizActivity.this, InstructorMainActivity.class));
            }
            else {
                Toast.makeText(InstructorAddQuizActivity.this, "Quiz exist", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}