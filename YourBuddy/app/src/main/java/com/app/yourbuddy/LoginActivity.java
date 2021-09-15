package com.app.yourbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Button btnRegister = (Button)findViewById(R.id.register);
        Button btnLogin = (Button)findViewById(R.id.login);
        final EditText e_email = findViewById(R.id.email);
        final EditText e_password = findViewById(R.id.password);
        pref = getSharedPreferences("YourBuddy",
                Context.MODE_PRIVATE);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e_email.getText().toString();
                String password = e_password.getText().toString();
                boolean isValid = validate(email, password, e_email, e_password);
                if(!isValid) {
                    Toast.makeText(LoginActivity.this, "Input validation error", Toast.LENGTH_LONG).show();
                    return;
                }
                boolean isValidUser = databaseHelper.validateUser(email,password);

                if(isValidUser) {
                    Toast.makeText(LoginActivity.this, "Login successful!!", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username",email);
                    editor.putString("password",password);
                    editor.commit();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validate(String email, String password, EditText e_email, EditText e_password) {
        boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            e_email.setError("Email id is mandatory");
            isValid = false;
        }
        if (TextUtils.isEmpty(password)) {
            e_password.setError("Password is mandatory");
            isValid = false;
        }
        return isValid;
    }
}
