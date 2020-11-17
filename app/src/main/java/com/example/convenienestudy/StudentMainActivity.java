package com.example.convenienestudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentMainActivity extends AppCompatActivity {


    //TODO STUDENT PAGES WITH COMPLETED ASSIGNMENT AND NON-COMPLETED ASSIGNMENT
    //TODO ABLE TO DO NON-COMPLETE ASSIGNMENT AND UPDATE ACCORDINGLY
    //TODO ABLE TO CHECK THE FEEDBACK IF THE ASSIGNMENT IS DONE
    private ArrayList<Assignment> completedAssignment, incompleteAssignment;
    private HashMap<String, Quiz> completedQuiz, incompleteQuiz;
    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private DatabaseReference userRef = usersRef.child(userId);
    private RecyclerView completedQuizRV, incompleteQuizRV;
    private RecyclerViewAdapaterStudentCompletedQuiz completedQuizAdapter, incompleteQuizAdapter;
    private EditText welcome_name;
    private String schoolId;
    private SharedPreferences mPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        welcome_name = (EditText) findViewById(R.id.welcome_name);

        completedAssignment = new ArrayList<Assignment>();
        incompleteAssignment = new ArrayList<Assignment>();
        completedQuiz = new HashMap<String, Quiz>();
        incompleteQuiz = new HashMap<String, Quiz>();

        completedQuizRV = (RecyclerView) findViewById(R.id.recyclerView_completedQuiz);
        completedQuizAdapter = new RecyclerViewAdapaterStudentCompletedQuiz(this, completedQuiz, completedAssignment);
        completedQuizRV.setLayoutManager(new LinearLayoutManager(this));
        completedQuizRV.setAdapter(completedQuizAdapter);

        incompleteQuizRV = (RecyclerView) findViewById(R.id.recyclerView_uncompletedQuiz);
        incompleteQuizAdapter = new RecyclerViewAdapaterStudentCompletedQuiz(this, incompleteQuiz, incompleteAssignment);
        incompleteQuizRV.setLayoutManager(new LinearLayoutManager(this));
        incompleteQuizRV.setAdapter(incompleteQuizAdapter);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                welcome_name.setText(name);
                for (DataSnapshot ds: snapshot.child("listOfAssignment").getChildren()){
                    Assignment tempAssignment = ds.getValue(Assignment.class);
                    boolean completed = tempAssignment.isCompleted();
                    Log.d("STUDENT MAIN ACTIVITY", "VALUE OF COMPLETED" + String.valueOf(completed));
                    Log.d("STUDENT MAIN ACTIVITY", tempAssignment.toString());
                    if(completed){
                        completedAssignment.add(tempAssignment);
                    }
                    else{
                        incompleteAssignment.add(tempAssignment);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mPreference = getSharedPreferences(LoginActivity.sharedPreFile, MODE_PRIVATE);
        schoolId = mPreference.getString(LoginActivity.schoolIdKey, "EMPTY");
        Query instructorQuery = usersRef.orderByChild("schoolId").equalTo(schoolId);
        instructorQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.hasChild("instructorId")){
                        for (DataSnapshot ds2: ds.child("lstOfQuiz").getChildren()){
                            for (Assignment a : incompleteAssignment){
                                String tempQuizNumber = a.getQuizNumber();
                                if (tempQuizNumber.equals(String.valueOf(ds2.child("quizNumber").getValue(int.class)))){
                                    incompleteQuiz.put(tempQuizNumber, ds2.getValue(Quiz.class));
                                }
                                else{
                                    completedQuiz.put(tempQuizNumber, ds2.getValue(Quiz.class));
                                }
                            }
                        }
                        completedQuizRV.setAdapter(completedQuizAdapter);
                        incompleteQuizRV.setAdapter(incompleteQuizAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}