package com.example.convenienestudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InstructorQuizActivity extends AppCompatActivity {

    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private TextView quizTitle, quizScore, quizDescription;
    private List<Question> lstQuestion;
    private RecyclerView questionRV;
    private RecyclerViewAdapterInstructorQuestion questionAdapter;
    private Button addQuestionButton, deleteQuestionButton, deleteQuizButton;
    private DatabaseReference quizRef, questionRef;
    private Quiz quiz;
    private String quizNumberString;
    private int quizNumber, instructorId;
    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_quiz);

        quizTitle = (TextView) findViewById(R.id.quizTitle_id);
        quizScore = (TextView) findViewById(R.id.quizScore_id);
        quizDescription = (TextView) findViewById(R.id.quizDescription_id);
        lstQuestion = new ArrayList<Question>();

        mPreferences = getSharedPreferences(LoginActivity.sharedPreFile, MODE_PRIVATE);
        instructorId = mPreferences.getInt(LoginActivity.instructorIdKey, 0);

        addQuestionButton = (Button) findViewById(R.id.addQuestionButton);
        deleteQuestionButton = (Button) findViewById(R.id.deleteQuestionButton);
        deleteQuizButton = (Button) findViewById(R.id.deleteQuizButton);

        addQuestionButton.setOnClickListener(addQuestionListener);
        deleteQuestionButton.setOnClickListener(deleteQuestionListener);
        deleteQuizButton.setOnClickListener(deleteQuizListener);

        Intent intent = getIntent();
        quiz = intent.getExtras().getParcelable("quizObject");

        quizNumberString = quiz.getQuizNumberString();
        quizNumber = quiz.getQuizNumber();

        quizRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("lstOfQuiz");

        String title = quiz.getTitle();
        String description = quiz.getDescription();

        questionRef = quizRef.child(quizNumberString).child("listOfQuestion");
        questionRef.addValueEventListener(questionListener);

        String score = quiz.getScoreToString();

        quizTitle.setText(title);
        quizScore.setText(score);
        quizDescription.setText(description);

        questionRV = (RecyclerView) findViewById(R.id.recyclerviewQuestion_id);
        questionAdapter  = new RecyclerViewAdapterInstructorQuestion(this, lstQuestion);
        questionRV.setLayoutManager(new LinearLayoutManager(this));
        questionRV.setAdapter(questionAdapter);
    }

    View.OnClickListener addQuestionListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(InstructorQuizActivity.this, InstructorAddQuestion.class);
            intent.putExtra("quizObject", quiz);
            intent.putExtra("questionId", lstQuestion.size());
            startActivity(intent);
        }
    };

    View.OnClickListener deleteQuestionListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            startActivity(new Intent(InstructorQuizActivity.this, InstructorDeleteQuestion.class));
        }
    };

    View.OnClickListener deleteQuizListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            quizRef.child(quizNumberString).setValue(null);
            quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()){
                        Quiz temp = ds.getValue(Quiz.class);
                        int tempNumber = temp.getQuizNumber();
                        if (tempNumber > quizNumber){
                            quizRef.child(Integer.toString(tempNumber)).setValue(null);
                            quizRef.child(Integer.toString(tempNumber-1)).setValue(new Quiz(temp.getTitle(), temp.getDescription(), tempNumber-1, instructorId));
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            startActivity(new Intent(InstructorQuizActivity.this, InstructorMainActivity.class));
        }
    };

    ValueEventListener questionListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Question temp = ds.getValue(Question.class);
                    if (!lstQuestion.contains(temp)){
                        lstQuestion.add(temp);
                        temp.updateQuiz(quiz);
                        quizScore.setText(quiz.getScoreToString());
                    }
                }
                questionRV.setAdapter(questionAdapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w("loadPost:onCancelled", error.toException());
        }
    };

}