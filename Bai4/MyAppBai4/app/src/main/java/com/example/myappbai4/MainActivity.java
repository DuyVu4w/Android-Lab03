package com.example.myappbai4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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


        TextView tvUserName = findViewById(R.id.userName);
        TextView tvJob = findViewById(R.id.tvJob);
        ImageView ivEdit = findViewById(R.id.ivEdit);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvPhone = findViewById(R.id.tvPhone);
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvAddress = findViewById(R.id.tvAddress);
        TextView tvHomePage = findViewById(R.id.tvHomePage);

        Intent dataIntent = getIntent();
        if (getIntent() != null && dataIntent.hasExtra("job")) {
            tvJob.setText(dataIntent.getStringExtra("job"));
            tvName.setText(dataIntent.getStringExtra("name"));
            tvPhone.setText(dataIntent.getStringExtra("phone"));
            tvEmail.setText(dataIntent.getStringExtra("email"));
            tvAddress.setText(dataIntent.getStringExtra("address"));
            tvHomePage.setText(dataIntent.getStringExtra("homePage"));
        }


        ivEdit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra("userName", tvUserName.getText().toString().trim());
            intent.putExtra("job", tvJob.getText().toString().trim());
            intent.putExtra("name", tvName.getText().toString().trim());
            intent.putExtra("phone", tvPhone.getText().toString().trim());
            intent.putExtra("email", tvEmail.getText().toString().trim());
            intent.putExtra("address", tvAddress.getText().toString().trim());
            intent.putExtra("homePage", tvHomePage.getText().toString().trim());

            startActivity(intent);
        });

    }
}