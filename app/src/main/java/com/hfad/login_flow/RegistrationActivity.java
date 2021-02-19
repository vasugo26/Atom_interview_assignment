package com.hfad.login_flow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

    }

    public void onBack(View view) {
        finish();
    }

    public void onContinueWithName(View view) {

        EditText name_field = (EditText) findViewById(R.id.name);
        String name = name_field.getText().toString();
        Toast.makeText(RegistrationActivity.this, "Welcome, "+ name,
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegistrationActivity.this, HomeScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }

}
