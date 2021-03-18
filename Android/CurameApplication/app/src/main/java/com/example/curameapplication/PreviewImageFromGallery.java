package com.example.curameapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class PreviewImageFromGallery extends AppCompatActivity {

    private ImageView imagePreview;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image_from_gallery);

        imagePreview = (ImageView) findViewById(R.id.imagePreview);

        //Get Image taken from camera
        imageUri = (Uri) getIntent().getParcelableExtra("IMAGE_DATA");

        //Set Image
        imagePreview.setImageURI(null);
        imagePreview.setImageURI(imageUri);

    }

    public void cancelImage(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public void selectImage(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("IMAGE_URI", imageUri);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}