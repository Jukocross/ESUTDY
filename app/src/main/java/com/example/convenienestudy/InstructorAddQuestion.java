package com.example.convenienestudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class InstructorAddQuestion extends AppCompatActivity {

    //TODO SET THE IDCOUNTER TO THE BIGGER NUMBER WITHIN THE ARRAY

    private Button createQuestionButton;
    private TextInputLayout question,answer, score, choice1, choice2, choice3, choice4;
    private DatabaseReference questionRef, quizRef;
    private Intent intent;
    private String questionString, answerString, quizTitle, mcq1, mcq2, mcq3, mcq4;
    private int questionScore, questionId;
    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_add_question);

        Intent getIntent = getIntent();
        quiz = getIntent.getExtras().getParcelable("quizObject");
        questionId = getIntent.getIntExtra("questionId", 0);

        quizTitle = quiz.getTitle();
        createQuestionButton = (Button) findViewById(R.id.createQuestion_Button);
        question = (TextInputLayout) findViewById(R.id.createQuestion_Question);
        answer = (TextInputLayout) findViewById(R.id.createQuestion_Answer);
        score = (TextInputLayout) findViewById(R.id.createQuestion_Score);
        choice1 = (TextInputLayout) findViewById(R.id.createQuestion_Choice1);
        choice2 = (TextInputLayout) findViewById(R.id.createQuestion_Choice2);
        choice3 = (TextInputLayout) findViewById(R.id.createQuestion_Choice3);
        choice4 = (TextInputLayout) findViewById(R.id.createQuestion_Choice4);

        createQuestionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                intent = new Intent(InstructorAddQuestion.this, InstructorQuizActivity.class);
                quizRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("lstOfQuiz").child(quiz.getQuizNumberString());
                questionRef = quizRef.child("listOfQuestion");
                questionString = question.getEditText().getText().toString();
                answerString = answer.getEditText().getText().toString();
                questionScore = Integer.parseInt(score.getEditText().getText().toString());
                mcq1 = choice1.getEditText().getText().toString();
                mcq2 = choice2.getEditText().getText().toString();
                mcq3 = choice3.getEditText().getText().toString();
                mcq4 = choice4.getEditText().getText().toString();
                questionRef.addListenerForSingleValueEvent(checkForChild);
            }
        });

    }

    ValueEventListener checkForChild = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            //TODO MAKE SURE NO DUPLICATE QUESTION
            if (!snapshot.hasChild(Integer.toString(questionId))){
                questionRef.child(Integer.toString(questionId)).setValue(new Question(questionString, questionScore, answerString,mcq1, mcq2, mcq3, mcq4, quiz));
                quizRef.child("totalScore").setValue(quiz.getTotalScore());
                intent.putExtra("quizObject", quiz);
                startActivity(intent);
            }
            else {
                Toast.makeText(InstructorAddQuestion.this, "Question exist", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

}