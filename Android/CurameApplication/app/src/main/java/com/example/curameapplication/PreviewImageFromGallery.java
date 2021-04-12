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

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.tensorflow.lite.support.common.FileUtil.loadMappedFile;

public class PreviewImageFromGallery extends AppCompatActivity {
    private static final int RESULT_CODE_EXIT = 2;

    private ImageView imagePreview;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image_from_gallery);

        //Find views
        imagePreview = (ImageView) findViewById(R.id.imagePreview);

        //Get Image taken from camera
        imageUri = (Uri) getIntent().getParcelableExtra("IMAGE_DATA");

        //Set Image using picasso
        Picasso
                .get()
                .load(imageUri)
                .placeholder(R.drawable.empty)
                .fit()
                .centerInside()
                .into(imagePreview);
    }//onCreate

    //Cancel the previewed image
    public void cancelImage(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }//cancelImage

    //Accept the previewed image to be classified
    public void selectImage(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("IMAGE_URI", imageUri);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }//selectImage

    //Finish the activity
    public void finishActivity(View view) {
        Intent returnIntent = new Intent();
        setResult(RESULT_CODE_EXIT, returnIntent);
        finish();
    }//finishActivity
}