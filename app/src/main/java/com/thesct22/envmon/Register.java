package com.thesct22.envmon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText username,email,password;
    FirebaseFirestore fstore;
    private FirebaseAuth mAuth;
    CheckBox checkBoxAdmin;

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
        //private EditText password2;
        Button bRegister = findViewById(R.id.btnRegister);
        ProgressBar progressBarR = findViewById(R.id.progressBar2);

        bRegister.setOnClickListener(view -> {
            boolean validate;
            validate = checkField(username);
            validate&=checkField(email);
            validate&=checkField(password);

            if(validate){
                mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(authResult -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                    assert user != null;
                    DocumentReference df=fstore.collection("Users").document(user.getUid());
                    Map<String,Object> userInfo =new HashMap<>();
                    userInfo.put("Username", username.getText().toString());
                    userInfo.put("Email", email.getText().toString());
                    userInfo.put("isAdmin", checkBoxAdmin.isChecked());

                    df.set(userInfo);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }).addOnFailureListener(e -> Toast.makeText(Register.this, "Failed to create Account", Toast.LENGTH_SHORT).show());
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
            return false;
        }
        return true;
    }
}