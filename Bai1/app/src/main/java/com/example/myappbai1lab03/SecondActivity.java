package com.example.myappbai1lab03;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.second_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.second), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView title = findViewById(R.id.tvWelcome);
        EditText etName = findViewById(R.id.etName);
        Button btnExit = findViewById(R.id.btnExit);

        String email = getIntent().getStringExtra(MainActivity.key);

        title.setText("Xin Chào, " + email + ". Vui lòng nhập tên");
        btnExit.setOnClickListener(v -> {
            String name = etName.getText().toString();

            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            intent.putExtra(MainActivity.key, name);

            startActivity(intent);
        });
    }
}