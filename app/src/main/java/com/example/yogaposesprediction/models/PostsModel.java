package com.example.yogaposesprediction.models;

public class PostsModel {
    private int postDescription;
    private int postImage;

    public PostsModel() {
    }

    public PostsModel(int postDescription, int postImage) {
        this.postDescription = postDescription;
        this.postImage = postImage;
    }

    public int getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(int postDescription) {
        this.postDescription = postDescription;
    }

    public int getPostImage() {
        return postImage;
    }

    public void setPostImage(int postImage) {
        this.postImage = postImage;
    }
}

