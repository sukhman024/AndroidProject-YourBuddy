package com.app.yourbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText e_email = findViewById(R.id.email);
        final EditText e_password = findViewById(R.id.password);
        final EditText e_firstName = findViewById(R.id.first_name);
        final EditText e_lastName = findViewById(R.id.last_name);
        final EditText e_contact = findViewById(R.id.contact_no);

        Button btnRegister = (Button) findViewById(R.id.register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e_email.getText().toString();
                String password = e_password.getText().toString();
                String fName = e_firstName.getText().toString();
                String lName = e_lastName.getText().toString();
                String contactNo = e_contact.getText().toString();
                boolean isValid = validateInput(email, password, fName, lName, contactNo, e_email, e_password, e_firstName, e_lastName, e_contact);
                if(!isValid) {
                    Toast.makeText(RegisterActivity.this, "Input validation error", Toast.LENGTH_LONG).show();
                    return;
                }
                boolean isUserAdded = databaseHelper.addUser(fName + " " + lName,email,contactNo,password);
                if(isUserAdded) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful!!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "Error: Please try again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean validateInput(String email, String password, String fName, String lName, String contactNo, EditText e_email, EditText e_password, EditText e_firstName, EditText e_lastName, EditText e_contactNo) {
        boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            e_email.setError("Email id is mandatory");
            isValid = false;
        }
        if (TextUtils.isEmpty(password)) {
            e_password.setError("Password is mandatory");
            isValid = false;
        }
        if (TextUtils.isEmpty(fName)) {
            e_firstName.setError("First Name is mandatory");
            isValid = false;
        }
        if (TextUtils.isEmpty(lName)) {
            e_lastName.setError("Last Name is mandatory");
            isValid = false;
        }
        if (TextUtils.isEmpty(contactNo)) {
            e_contactNo.setError("Contact No is mandatory");
            isValid = false;
        }
        return isValid;
    }
}
