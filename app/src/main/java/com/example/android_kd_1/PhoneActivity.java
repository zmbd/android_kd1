package com.example.android_kd_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PhoneActivity extends AppCompatActivity {
    private EditText phoneDisplay;
    private StringBuilder phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        phoneDisplay = findViewById(R.id.phoneDisplay);
        phoneNumber = new StringBuilder();
        phoneDisplay.setEnabled(false);

        // Setup number buttons
        for (int i = 0; i <= 9; i++) {
            final int number = i;
            int buttonId = getResources().getIdentifier(
                    "button" + i, "id", getPackageName()
            );
            Button button = findViewById(buttonId);
            button.setOnClickListener(v -> {
                if (phoneNumber.length() < 10) {
                    phoneNumber.append(number);
                    phoneDisplay.setText(phoneNumber.toString());
                }
            });
        }

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("phone_number", phoneNumber.toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}