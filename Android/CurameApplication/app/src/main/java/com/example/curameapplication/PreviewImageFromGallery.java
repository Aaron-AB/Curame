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

import static org.tensorflow.lite.support.common.FileUtil.loadMappedFile;

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


        /**
        try {
            this.classifyImage();
        } catch (Exception e) {
            Log.d("debug", "onCreate: " + e.getMessage());
        }
         **/
        String imageFilePath = "/sdcard/Download/ISIC_0024898.jpg";
        Log.d("IMAGE URI", "OVER HERE " + imageFilePath);

        ImageClassifier classifier = new ImageClassifier(this, imageFilePath);
        String values = classifier.Classify();
        //List<String> probabilities = classifier.Classify();

        Log.d("debug", "classifyImage: " + values);


    }

    private void classifyImage() {
        //String imageFilePath = imageUri.getPath();
        String imageFilePath = "/sdcard/Download/ISIC_0024898.jpg";
        Log.d("IMAGE URI", "OVER HERE " + imageFilePath);

        ImageClassifier classifier = new ImageClassifier(this, imageFilePath);

        //List<String> probabilities = classifier.Classify();

        //Log.d("debug", "classifyImage: " + probabilities);

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