package com.example.curameapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class CameraActivity extends AppCompatActivity {

    ImageView scanImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        scanImage = (ImageView)findViewById(R.id.scanImage);

        //Start Camera Application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Retrieve the image from the camera activity
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        scanImage.setImageBitmap(bitmap);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("IMAGE_DATA", bitmap);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void selectImage(View view) {

    }
}