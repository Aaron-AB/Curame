package com.example.curameapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class PreviewImageFromGallery extends AppCompatActivity {

    private ImageView imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image_from_gallery);

        imagePreview = (ImageView) findViewById(R.id.imagePreview);

        //Get Image taken from camera
        Uri imageUri = (Uri) getIntent().getParcelableExtra("IMAGE_DATA");

        //Set Image
        imagePreview.setImageURI(null);
        imagePreview.setImageURI(imageUri);

    }
}