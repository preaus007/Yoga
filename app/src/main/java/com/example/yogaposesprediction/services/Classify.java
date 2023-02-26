package com.example.yogaposesprediction.services;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yogaposesprediction.R;
import com.example.yogaposesprediction.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Classify extends AppCompatActivity {

    TextView result;
    ImageView image;
    AppCompatButton predict, shareResult;
    Bitmap bitmap;
    int imageSize = 224;

    Uri image_uri = null;
    private static final int PERMISSION_CODE = 100;
    private static final int GALLERY_IMAGE_CODE = 200;
    private static final int CAMERA_IMAGE_CODE = 300;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);

        getPermission();

        image = findViewById(R.id.image);
        predict = findViewById(R.id.predict);
        result = findViewById(R.id.result);
        shareResult = findViewById(R.id.share_result);
        shareResult.setVisibility(View.GONE);

        getPermission();

        image.setOnClickListener(v -> imagePicDialog());

        predict.setOnClickListener(v -> {
            if(bitmap != null) {
                classifyImage(bitmap);
            }
            else{
                Toast.makeText(Classify.this, "Select image first", Toast.LENGTH_SHORT).show();
            }
        });

        shareResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(result.getText().toString())){
                    Toast.makeText(Classify.this, result.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Classify.this, new String[]{Manifest.permission.CAMERA},11);
            }
        }
    }

    private void imagePicDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose image from...");

        builder.setItems(options, (dialog, which) -> {
            if(which == 0){
                cameraPick();
            }
            else {
                galleryPick();
            }
        });

        builder.create().show();
    }

    private void galleryPick() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_IMAGE_CODE);
    }

    private void cameraPick() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_IMAGE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==PERMISSION_CODE){
            if (grantResults.length>0){
                if (grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    this.getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GALLERY_IMAGE_CODE){
            if (data != null){
                image_uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),image_uri);
                    image.setImageBitmap(bitmap);
                    bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == CAMERA_IMAGE_CODE) {
            bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);
            bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(Classify.this);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for (int j=0; j < imageSize; j++){
                    int val = intValues[pixel++];//RGB
                    byteBuffer.putFloat(((val >> 16)& 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8)& 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            String[] classes = {"Tree","Down dog","Goddess","Plank","Warrior"};

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float [] probability = outputFeature0.getFloatArray();

            int max = 0;
            float maxprob = 0;
            for (int i = 0; i<probability.length; i++){
                if(probability[i] > maxprob){
                    maxprob = probability[i];
                    max = i;
                }
            }

            String final_result = getString(R.string.result, classes[max], maxprob);
            result.setText(final_result);
            shareResult.setVisibility(View.VISIBLE);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }
}