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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructorQuizActivity extends AppCompatActivity {

    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private TextView quizTitle, quizScore, quizDescription;
    private List<Question> lstQuestion;
    private HashMap<String, String> lstStudentId;
    private RecyclerView questionRV;
    private RecyclerViewAdapterInstructorQuestion questionAdapter;
    private Button addQuestionButton, deleteQuestionButton, deleteQuizButton, publishQuizButton, feedbackQuizButton;
    private DatabaseReference usersRef, quizRef, questionRef;
    private Quiz quiz;
    private String quizNumberString, instructorId, schoolId;
    private int quizNumber;
    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_quiz);

        quizTitle = (TextView) findViewById(R.id.quizTitle_id);
        quizScore = (TextView) findViewById(R.id.quizScore_id);
        quizDescription = (TextView) findViewById(R.id.quizDescription_id);
        lstQuestion = new ArrayList<Question>();
        lstStudentId = new HashMap<String, String>();

        mPreferences = getSharedPreferences(LoginActivity.sharedPreFile, MODE_PRIVATE);
        instructorId = mPreferences.getString(LoginActivity.instructorIdKey, "EMPTY");
        schoolId = mPreferences.getString(LoginActivity.schoolIdKey, "EMPTY");

        addQuestionButton = (Button) findViewById(R.id.addQuestionButton);
        deleteQuestionButton = (Button) findViewById(R.id.deleteQuestionButton);
        deleteQuizButton = (Button) findViewById(R.id.deleteQuizButton);
        publishQuizButton = (Button) findViewById(R.id.publishQuizButton);
        feedbackQuizButton = (Button) findViewById(R.id.feedbackQuizButton);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        Query studentQuery = usersRef.orderByChild("schoolId").equalTo(schoolId);
        studentQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.hasChild("studentId")){
                        String tempString = ds.child("userId").getValue(String.class);
                        String tempInt = ds.child("studentId").getValue(String.class);
                        lstStudentId.put(tempString, tempInt);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Adding Student", error.getDetails());
            }
        });

        addQuestionButton.setOnClickListener(addQuestionListener);
        deleteQuestionButton.setOnClickListener(deleteQuestionListener);
        deleteQuizButton.setOnClickListener(deleteQuizListener);
        publishQuizButton.setOnClickListener(publishQuizListener);
        feedbackQuizButton.setOnClickListener(feedbackQuizListener);

        Intent intent = getIntent();
        quiz = intent.getExtras().getParcelable("quizObject");

        quizNumberString = quiz.getQuizNumberString();
        quizNumber = quiz.getQuizNumber();
        if (quiz.isQuizPublished()){
            publishQuizButton.setText("Unpublish Quiz");
        }


        quizRef = usersRef.child(userId).child("lstOfQuiz");

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
            int nextQuestionId = 0;
            if (!lstQuestion.isEmpty()){
                nextQuestionId = lstQuestion.get(lstQuestion.size()-1).getQuestionId() + 1;
                Log.d("Adding Question", "Value of id is " + nextQuestionId);
            }
            intent.putExtra("questionId", nextQuestionId);
            startActivity(intent);
        }
    };

    View.OnClickListener deleteQuestionListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            startActivity(new Intent(InstructorQuizActivity.this, InstructorDeleteQuestion.class));
        }
    };

    View.OnClickListener publishQuizListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (quiz.isQuizPublished()){//Unpublish
                for (Map.Entry<String, String> entry: lstStudentId.entrySet()){
                    usersRef.child(entry.getKey()).child("listOfAssignment").child(quizNumberString).setValue(null);
                }
                quiz.setQuizPublished(false);
            }
            else {//Publish
                if(!lstStudentId.isEmpty()) {
                    for (Map.Entry<String, String> entry : lstStudentId.entrySet()) {
                        usersRef.child(entry.getKey()).child("listOfAssignment").child(quizNumberString).setValue(new Assignment(quizNumberString, entry.getValue()));
                    }
                    quiz.setQuizPublished(true);
                }
            }
            quizRef.child(quizNumberString).child("quizPublished").setValue(quiz.isQuizPublished());
            Intent tempIntent = new Intent(InstructorQuizActivity.this, InstructorQuizActivity.class);
            tempIntent.putExtra("quizObject", quiz);
            startActivity(tempIntent);
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
                            quizRef.child(String.valueOf(tempNumber)).setValue(null);
                            quizRef.child(String.valueOf(tempNumber-1)).setValue(new Quiz(temp.getTitle(), temp.getDescription(), tempNumber-1, instructorId));
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            if(quiz.isQuizPublished()){
                for (final Map.Entry<String, String> entry : lstStudentId.entrySet()){
                    usersRef.child(entry.getKey()).child("listOfAssignment").child(quiz.getQuizNumberString()).setValue(null);
                    usersRef.child(entry.getKey()).child("listOfAssignment").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()){
                                Assignment tempAssignment = ds.getValue(Assignment.class);
                                int tempAssignmentNumber = Integer.parseInt(tempAssignment.getQuizNumber());
                                if (tempAssignmentNumber > quizNumber){
                                    usersRef.child(entry.getKey()).child("listOfAssignment").child(String.valueOf(tempAssignmentNumber)).setValue(null);
                                    usersRef.child(entry.getKey()).child("listOfAssignment").child(String.valueOf(tempAssignmentNumber-1)).setValue(new Assignment(String.valueOf(tempAssignmentNumber-1), tempAssignment.getStudentId()));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            startActivity(new Intent(InstructorQuizActivity.this, InstructorMainActivity.class));
        }
    };

    View.OnClickListener feedbackQuizListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent feedbackIntent = new Intent(InstructorQuizActivity.this, InstructorQuizStudentFeedback.class);
            feedbackIntent.putExtra("listOfStudent", (Serializable) lstStudentId);
            startActivity(feedbackIntent);
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