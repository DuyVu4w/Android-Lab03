package com.example.myappbai1lab03;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final String key = "Key value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        EditText etEmail = findViewById(R.id.etEmail);
        TextView tvTitle = findViewById(R.id.tvWelcome);
        Button btn = findViewById(R.id.btnLogin);
        String name = getIntent().getStringExtra(key);

        if (name != null) {
            tvTitle.setText("Hẹn gặp lại");
            etEmail.setText(name);
            etEmail.setFocusable(false);
            btn.setVisibility(TextView.GONE);
        }

        btn.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra(key, email);
            startActivity(intent);
        });
    }
}