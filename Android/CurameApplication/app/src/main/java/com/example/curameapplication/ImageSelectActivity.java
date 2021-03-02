package com.example.curameapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageSelectActivity extends AppCompatActivity {

    ImageView scanImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        scanImage = (ImageView)findViewById(R.id.scanImage);

        //Get Image taken from camera
        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("IMAGE_DATA");

        //Set Image
        scanImage.setImageBitmap(bitmap);
    }
}