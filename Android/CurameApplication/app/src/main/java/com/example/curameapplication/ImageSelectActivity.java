package com.example.curameapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ImageSelectActivity extends AppCompatActivity {

    private ImageView scanImage;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        scanImage = (ImageView)findViewById(R.id.imagePreview);

        //Get Image taken from camera
        //Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("IMAGE_DATA");

        //Set Image
        //scanImage.setImageBitmap(bitmap);

        //Get Image taken from camera
        filePath = getIntent().getStringExtra("IMAGE_DATA");

        //Set Image
        Log.d("Debug", "Camera Activity: " + filePath);
        //Bitmap bitmap = getScaledBitmap(scanImage, filePath);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        scanImage.setImageBitmap(bitmap);
    }

    private Bitmap getScaledBitmap(ImageView imageView, String imagePath) {

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;

        int scale = Math.min(
                bitmapOptions.outWidth / imageView.getWidth(),
                bitmapOptions.outHeight / imageView.getHeight()
        );

        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inPurgeable = true;
        bitmapOptions.inSampleSize = scale;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bitmapOptions);

        return bitmap;
    }

    public void finishActivity(View view) {
        finish();
    }
}

