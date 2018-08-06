package com.akandro.www.ghrcediary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by akshay on 8/6/2018.
 */

public class SignUpActivity extends AppCompatActivity  {

    private TextView sign_in;

    private Button signupBtn;
    private EditText userNew;
    private EditText passwordNew;
    private EditText RePassword;
    private EditText emailNew;
    private String email,password, name;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup_fragment);
        progressDialog =new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();


       // sign_in=(TextView) findViewById(R.id.sign_in);
        userNew = (EditText) findViewById(R.id.userNew);
        emailNew = (EditText) findViewById(R.id.emailNew);
        passwordNew = (EditText) findViewById(R.id.passwordNew);
        RePassword = (EditText) findViewById(R.id.RePassword);
        signupBtn = (Button)  findViewById(R.id.signupBtn);



        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Login Please Wait...");
                progressDialog.show();
                sign_up();
            }
        });
    }


    public void registerUser(){

        email = emailNew.getText().toString().trim();
        password = passwordNew.getText().toString().trim();
        name = userNew.getText().toString().trim();



        //checking if email pass empty
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        // if not empty
        //display progress dialog

        progressDialog.setMessage("Login Please Wait...");
        progressDialog.show();
    }

    private void sign_up(){

        email = emailNew.getText().toString().trim();
        password = passwordNew.getText().toString().trim();
        name = userNew.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            // start activity
                            progressDialog.dismiss();
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignUpActivity.this, "User with this email already exist", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            final FirebaseUser user = task.getResult().getUser();
                            if (user != null) {
                                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                           // DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                                           // databaseReference = databaseReference.child(user.getUid());
                                            //to store data
                                            //new class is created UserModel in java class folder
                                            userModel userModel = new userModel();
                                            userModel.setName(name);
                                            userModel.setEmail(email);
                                            userModel.setPassword(password);
                                           // databaseReference.setValue(userModel);
                                            progressDialog.dismiss();
                                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    }
                                });

//                            finish();
//                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            }

                        }
                    }

                });
    }
}
