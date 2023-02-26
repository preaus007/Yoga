package com.example.yogaposesprediction.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yogaposesprediction.R;
import com.example.yogaposesprediction.models.PostsModel;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    ArrayList<PostsModel>posts;
    Context context;

    public PostsAdapter(ArrayList<PostsModel>posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostsModel postsList = posts.get(position);

        holder.postDescription.setText(postsList.getPostDescription());
        holder.postImage.setImageResource(postsList.getPostImage());

        holder.itemView.setOnClickListener(v -> Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show());
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView postImage;
        TextView postDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
            postDescription = itemView.findViewById(R.id.post_description);
        }
    }
}
