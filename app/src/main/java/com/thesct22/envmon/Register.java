package com.thesct22.envmon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText username,email,password;
    //private EditText password2;
    private Button bRegister;
    private ProgressBar progressBarR;
    FirebaseFirestore fstore;
    private FirebaseAuth mAuth;
    CheckBox checkBoxAdmin;
    boolean valid = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        checkBoxAdmin=findViewById(R.id.isAdmin);
        username=findViewById(R.id.etUsername);
        email=findViewById(R.id.etEmail);
        password=findViewById(R.id.etRPassword);
        //checkField()
        //password2=findViewById(R.id.etRPassword2);
        bRegister=findViewById(R.id.btnRegister);
        progressBarR=findViewById(R.id.progressBar2);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(username);
                checkField(email);
                checkField(password);

                if(valid){
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register.this, "Account Created ", Toast.LENGTH_SHORT).show();
                            DocumentReference df=fstore.collection("Users").document(user.getUid());
                            Map<String,Object> userInfo =new HashMap<>();
                            userInfo.put("Username", username.getText().toString());
                            userInfo.put("Email", email.getText().toString());
                            if(checkBoxAdmin.isChecked()){
                                userInfo.put("isAdmin", "1");
                            }
                            else{
                                userInfo.put("isAdmin", "0");
                            }

                            df.set(userInfo);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Failed to create Account", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });



//        bRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Register.this,Login.class);
//                startActivity(i);
//            }
//        });
    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }
}