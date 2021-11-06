package com.project.adverstir;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.widget.ContentLoadingProgressBar;

import com.example.adverstir.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private AppCompatEditText etName, etEmail, etMobile, etPassword, etConfirm;
    private String name, email, mobile, password, confirm;

    private int UserCount = 0;
    private ContentLoadingProgressBar progressBar;
    private FirebaseAuth mAuth;


    private boolean isValid(String mobileNumber) {
        Pattern mobileValidPattern = Pattern.compile("(0|91)?[7-9][0-9]{9}");
        Matcher m = mobileValidPattern.matcher(mobileNumber);
        return (m.find() && m.group().equals(mobileNumber));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);

        etMobile = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirm = findViewById(R.id.etConfirmPassword);

        progressBar = findViewById(R.id.progressBar);
        progressBar.hide();


        AppCompatButton btnAdd = findViewById(R.id.btnRegister);
        btnAdd.setOnClickListener(v -> {
            name = Objects.requireNonNull(etName.getText()).toString();
            email = Objects.requireNonNull(etEmail.getText()).toString();
            mobile = Objects.requireNonNull(etMobile.getText()).toString();
            password = Objects.requireNonNull(etPassword.getText()).toString();
            confirm = Objects.requireNonNull(etConfirm.getText()).toString();


            if (TextUtils.isEmpty(name)) {
                etName.setError("This field is empty!");
                return;
            }
            // blan space is for error messages
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("This field is empty!");
                return;
            }
            if (!isValid(mobile)) {
                etMobile.setError("the Phone number length should be 10");
                return;
            }
            if (password.length() < 6) {
                etPassword.setError("Minimum Length 6");
                return;
            }
            if (!TextUtils.equals(password, confirm)) {
                etConfirm.setError("Password does not match !");
                return;
            }

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

            databaseReference.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                            dataSnapshot.getChildren().iterator().hasNext()) {
                        if (UserCount == 0) {
                            Toast.makeText(SignUpActivity.this, "Email already exist!", Toast.LENGTH_LONG).show();
                        } else if (UserCount == -1) {
                            UserCount = 0;
                        }

                    } else {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                        String userId = mDatabase.push().getKey();
                        User user = new User(userId, name, email, mobile);
                        mDatabase.child(Objects.requireNonNull(userId)).setValue(user);
                        progressBar.hide();
                        Toast.makeText(SignUpActivity.this, "User Added Successfully!", Toast.LENGTH_LONG).show();
                        etName.setText("");
                        etEmail.setText("");
                        etMobile.setText("");
                        etPassword.setText("");
                        etConfirm.setText("");
                        UserCount = -1;

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Intent i = new Intent(SignUpActivity.this, com.project.adverstir.LoginActivity.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            //   Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Intent i = new Intent(SignUpActivity.this, com.project.adverstir.LoginActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });


        });
    }
}
