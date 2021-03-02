package com.example.curameapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageButton scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanButton = (ImageButton)findViewById(R.id.scanButton);
    }//onCreate

    public void goToCamera(View view) {
        //Start Camera
        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
        startActivityForResult(intent, 1);
    }//goToCamera

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getParcelableExtra("IMAGE_DATA");

                //pass the image to the Image Selection activity
                Intent intent = new Intent(getApplicationContext(), ImageSelectActivity.class);
                intent.putExtra("IMAGE_DATA", bitmap);
                startActivity(intent);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //no result
            }
        }
    }//onActivityResult
}