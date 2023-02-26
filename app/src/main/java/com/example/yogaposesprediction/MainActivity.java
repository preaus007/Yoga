package com.example.yogaposesprediction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.yogaposesprediction.services.About;
import com.example.yogaposesprediction.services.BMI;
import com.example.yogaposesprediction.services.Blog;
import com.example.yogaposesprediction.services.Classify;
import com.example.yogaposesprediction.services.Map;


public class MainActivity extends AppCompatActivity {

    CardView blog, map, classify, bmi, qna, about;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blog = findViewById(R.id.blog);
        map = findViewById(R.id.map);
        classify = findViewById(R.id.classify);
        bmi = findViewById(R.id.bmi);
        qna = findViewById(R.id.qna);
        about = findViewById(R.id.about_me);

        blog.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Blog.class)));
        map.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Map.class)));
        classify.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Classify.class)));
        bmi.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), BMI.class)));
        qna.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Blog.class)));
        about.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), About.class)));

    }
}