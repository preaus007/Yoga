package com.example.yogaposesprediction.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yogaposesprediction.adapters.PostsAdapter;
import com.example.yogaposesprediction.models.PostsModel;
import com.example.yogaposesprediction.R;

import java.util.ArrayList;

public class Blog extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<PostsModel>posts;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        toolbar = findViewById(R.id.my_toolbar);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Blog Section");
        }

        posts = new ArrayList<>();
        for(int i=0; i<50; i++){
            posts.add(new PostsModel(R.string.test ,R.drawable.pose_01));
        }
//                new PostsModel("Yoga is good",R.drawable.pose_02),
//                new PostsModel("Yoga is good",R.drawable.pose_03),
//                new PostsModel("Yoga is good",R.drawable.pose_04),
//                new PostsModel("Yoga is good",R.drawable.pose_05),

        PostsAdapter myPostsAdapter = new PostsAdapter(posts,Blog.this);
        recyclerView.setAdapter(myPostsAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.signOut){
            Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        }
        else{
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}