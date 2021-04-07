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

        imagePreview = (ImageView) findViewById(R.id.imagePreview);

        //Get Image taken from camera
        imageUri = (Uri) getIntent().getParcelableExtra("IMAGE_DATA");

        //Set Image
        imagePreview.setImageURI(null);
        imagePreview.setImageURI(imageUri);

        /*
        String imageFilePath = "/sdcard/Android/data/com.example.curameapplication/files/Pictures/SCAN_18_03_2021_01_16_25/ISIC_0034114.jpg";
        Log.d("IMAGE URI", "OVER HERE " + imageFilePath);

        //Set the image to be classified
        ImageClassifier classifier = new ImageClassifier(this, imageFilePath);

        //Get the values from the classification
        Map<String, Float> valueMap = classifier.Classify();
        String values = valueMap.toString();

        Log.d("CLASSIFICATION", "classifyImage: " + values);
        */
    }

    /*
    private void classifyImage() {
        //String imageFilePath = imageUri.getPath();
        String imageFilePath = "/sdcard/Download/ISIC_0024898.jpg";
        Log.d("IMAGE URI", "OVER HERE " + imageFilePath);

        ImageClassifier classifier = new ImageClassifier(this, imageFilePath);

        //List<String> probabilities = classifier.Classify();

        //Log.d("debug", "classifyImage: " + probabilities);
    }*/

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

    public void finishActivity(View view) {
        Intent returnIntent = new Intent();
        setResult(RESULT_CODE_EXIT, returnIntent);
        finish();
    }
}