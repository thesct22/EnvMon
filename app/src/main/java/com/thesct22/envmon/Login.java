package com.thesct22.envmon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private EditText txtLEmail,txtLPassword;
    private ProgressBar progressBarL;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth= FirebaseAuth.getInstance();
        txtLEmail=findViewById(R.id.etLEmail);
        txtLPassword=findViewById(R.id.etLPassword);
        Button bLogin = findViewById(R.id.btnLogin);
        progressBarL=findViewById(R.id.progressBar);

        mAuthStateListener = firebaseAuth -> {
            FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
            if( mFirebaseUser != null ){
                Toast.makeText(Login.this,"You are logged in",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
            }
            else{
                Toast.makeText(Login.this,"Please Login", Toast.LENGTH_SHORT).show();
            }
        };

        bLogin.setOnClickListener(v -> {
            String email = txtLEmail.getText().toString();
            String pwd = txtLPassword.getText().toString();
            if(email.isEmpty()){
                txtLEmail.setError("Please enter email id");
                txtLEmail.requestFocus();
            }
            else if(pwd.isEmpty()){
                txtLPassword.setError("Please enter your password");
                txtLPassword.requestFocus();
            }

            else{
                mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(Login.this, task -> {
                    if(task.isSuccessful()){
                        finish();
                        startActivity(new Intent(Login.this, MainActivity.class));

                    }
                    else{
                        try {
                            throw Objects.requireNonNull(task.getException());
                        }
                        catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                        }
                        catch (FirebaseAuthEmailException e){
                            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                        }
                        catch (FirebaseAuthException e){
                            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

}