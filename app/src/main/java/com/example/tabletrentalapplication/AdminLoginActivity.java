package com.example.tabletrentalapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Initialize UI components
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);

        // Set up login button click listener
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Check hardcoded credentials
            if (username.equals("Admin") && password.equals("Admin1234")) {
                // Successful login
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Navigate to AdminActivity
                Intent intent = new Intent(AdminLoginActivity.this, AdminActivity.class);
                startActivity(intent);
                finish(); // Optional: Prevent returning to login screen
            } else {
                // Failed login
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}