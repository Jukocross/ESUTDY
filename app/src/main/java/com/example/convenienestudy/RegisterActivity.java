package com.example.convenienestudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailField, passwordField, nameField;
    private Button registerBtn;
    private Spinner userSpinner, schoolSpinner;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO UPDATE THE FIREBASE WITH THE CORRECT DATABASE REFERENCES
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        userSpinner = (Spinner) findViewById(R.id.register_userSpinner);
        schoolSpinner = (Spinner) findViewById(R.id.register_schoolSpinner);
        emailField = (EditText) findViewById(R.id.register_email_address);
        passwordField = (EditText) findViewById(R.id.register_password);
        registerBtn = (Button) findViewById(R.id.register_submit);
        nameField = (EditText) findViewById(R.id.register_name);


        ArrayAdapter<CharSequence> userSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.user_array, android.R.layout.simple_spinner_item);
        userSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(userSpinnerAdapter);

        ArrayAdapter<CharSequence> schoolSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.school_array, android.R.layout.simple_spinner_item);
        schoolSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(schoolSpinnerAdapter);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String name = nameField.getText().toString();

                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    String selectedSchool = schoolSpinner.getSelectedItem().toString();
                                    String selectedRole = userSpinner.getSelectedItem().toString();
                                    if (selectedRole == "Instructor"){
                                        DatabaseReference userRef = myRootRef.child("Users").child(userId);
                                        userRef.setValue(new Instructors(name, email,"1",userId));
                                    }
                                    DatabaseReference userRef =  myRootRef.child("Schools").child("SUTD").child("User Id");
                                    userRef.setValue(userId);

                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    Toast.makeText(getApplicationContext(),
                                            "Registration Successful. Please login.",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "E-mail or password is wrong", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                }
                            }
                        });
            }
        });

    }
}
