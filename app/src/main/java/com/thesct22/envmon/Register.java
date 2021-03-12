package com.thesct22.envmon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private EditText txtRUsername,txtREmail,txtRPassword;
    //private EditText txtRPassword2;
    private Button bRegister;
    private ProgressBar progressBarR;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        txtRUsername=findViewById(R.id.etUsername);
        txtREmail=findViewById(R.id.etEmail);
        txtRPassword=findViewById(R.id.etRPassword);
        //txtRPassword2=findViewById(R.id.etRPassword2);
        bRegister=findViewById(R.id.btnRegister);
        progressBarR=findViewById(R.id.progressBar2);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtREmail.getText().toString();
                String pwd = txtRPassword.getText().toString();
                if(email.isEmpty()){
                    txtREmail.setError("Please enter email id");
                    txtREmail.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    txtRPassword.setError("Please enter your password");
                    txtRPassword.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(Register.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Register.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                startActivity(new Intent(Register.this,MainActivity.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Register.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }
            }
        });

//        tvSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Register.this,Login.class);
//                startActivity(i);
//            }
//        });
    }
}