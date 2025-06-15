package com.s23010916.chanuka;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    EditText usernameTxt;
    EditText passwordTxt;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        usernameTxt = findViewById(R.id.username);
        passwordTxt = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);

        login();
    }

    public void login() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                if (!username.isEmpty() && !password.isEmpty()){
                    boolean isDataInserted = myDb.insertData(username, password);
                    if (isDataInserted) {
                        Toast.makeText(MainActivity.this, "Data inserted successfully.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, MapIntegration.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter Username and Password.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}