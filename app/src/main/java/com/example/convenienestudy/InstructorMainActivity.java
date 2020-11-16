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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InstructorMainActivity extends AppCompatActivity {

    private ArrayList<Quiz> lstQuiz;
    private RecyclerView quizRV;
    private FloatingActionButton btnFloating;
    private RecyclerViewAdapterInstructorQuiz quizAdapter;
    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("lstOfQuiz");

    private static final String TAG ="Instructor Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_main);

        lstQuiz = new ArrayList<>();
        quizRV = (RecyclerView) findViewById(R.id.instructorQuiz_recyclerview_id);
        quizAdapter = new RecyclerViewAdapterInstructorQuiz(this, lstQuiz);
        btnFloating = (FloatingActionButton) findViewById(R.id.instructorQuiz_btnFloating);

        quizRef.addValueEventListener(instructorQuizListener);

        quizRV.setLayoutManager(new LinearLayoutManager(this));
        quizRV.setAdapter(quizAdapter);

        quizRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(quizRV.SCROLL_STATE_DRAGGING==newState){
                    btnFloating.hide();
                }
                else {
                    btnFloating.show();
                }
            }
        });

        btnFloating.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(InstructorMainActivity.this, InstructorAddQuizActivity.class);
                intent.putExtra("numberOfQuiz",lstQuiz.size());
                startActivity(intent);
            }
        });

    }

    ValueEventListener instructorQuizListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Quiz temp = ds.getValue(Quiz.class);
                    if (!lstQuiz.contains(temp)){
                        lstQuiz.add(temp);
                    }
                }
                quizRV.setAdapter(quizAdapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d(TAG, error.toException().toString());
        }
    };
}