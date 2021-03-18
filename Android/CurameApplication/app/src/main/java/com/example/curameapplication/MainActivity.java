package com.example.curameapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSIONS_CAMERA = 1;
    private static final int REQUEST_CODE_CAPTURE = 2;
    private static final int REQUEST_CODE_PERMISSIONS_GALLERY = 3;
    private static final int REQUEST_CODE_IMAGE_PICK = 4;
    private static final String PREFERENCES_NAME = "ImagePreferences";
    private static final String TAG = "Debug";

    private ImageButton scanButton;
    private String currentImagePath;
    private int count = 0;

    //Shared preferences
    SharedPreferences imagePref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanButton = (ImageButton)findViewById(R.id.scanButton);

        //Initialise Shared PreferencesEditor
        imagePref = getApplicationContext().getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        prefEditor = imagePref.edit();
        //imagePref = getApplicationContext().getSharedPreferences("ImagePreferences", MODE_PRIVATE);
    }//onCreate

    public void goToCamera(View view) {
        //If permission is not granted for Camera and/or external storage, request user to grant permission to application
        if(ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    REQUEST_CODE_PERMISSIONS_CAMERA
            );
        }else {
            //if permissions are already granted, call the camera
            callCaptureImageIntent();
        }
    }//goToCamera

    public void goToGallery(View view) {
        //If permission is not granter for Gallery and/or external storage, request user to grant permission to application
        if(ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                        getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    REQUEST_CODE_PERMISSIONS_GALLERY
            );
        } else {
            //if permissions are already granted, call the camera
            callGalleryIntent();
        }
    }//goToGallery

    //This files names and create a file for an image taken
    private File createImageFile() throws IOException {
        //Name the file SCAN_dd_MM_yyyy_HH_mm_ss
        String fileName = "SCAN_"
                + new SimpleDateFormat(
                        "dd_MM_yyyy_HH_mm_ss", Locale.getDefault()
        ).format(new Date());

        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                fileName,
                ".jpg",
                directory
        );

        //currentImagePath = image.getAbsolutePath();
        return image;
    };

    //This function calls the camera intent which starts the device camera and awaits the image
    private void callCaptureImageIntent(){
        //create an intent to start the camera activity
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //create an empty image file
        if(intent.resolveActivity(getPackageManager()) != null) {
            File image = null;
            try {
                image = createImageFile();
            } catch (IOException exception){
                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }

            if (image != null) {
                //Get the uri for the image
                Uri imageUri = FileProvider.getUriForFile(
                        this,
                        "com.example.curameapplication.fileprovider",
                        image
                );

                //Place the uri information as extra information for the intent
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                //Save image path
                currentImagePath = image.getAbsolutePath();
                //imagePref.edit().putString("currentImagePath", image.getAbsolutePath());
                prefEditor.putString("currentImagePath", (String)image.getAbsolutePath());
                prefEditor.apply();

                //Log.d(TAG, "Before Activity: " + imagePref.getString("currentImagePath", "none found"));

                //start the intent
                //startActivityForResult(intent, REQUEST_CODE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent,  REQUEST_CODE_CAPTURE);
                }
            }
        }
    }//callCaptureImageIntent

    //This function calls the gallery intent which starts the device gallery and awaits the image selection
    private void callGalleryIntent(){
        //create an intent to start the camera activity
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);

        /*
            //CODE TO CREATE A NEW FILE FOR SELECT IMAGE
        */

    }//callGalleryIntent

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //If the user grants all permissions
        if(requestCode == REQUEST_CODE_PERMISSIONS_CAMERA && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                callCaptureImageIntent();
            } else {
                //If permissions were not granted, let the user know
                Toast.makeText(this, "Not all permissions were granted to Curame", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == REQUEST_CODE_PERMISSIONS_GALLERY && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                callGalleryIntent();
            } else {
                //If permissions were not granted, let the user know
                Toast.makeText(this, "Not all permissions were granted to Curame", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        /*
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

        }*/

        //After Camera Activity Terminates
        if(requestCode == REQUEST_CODE_CAPTURE){
            if(resultCode == RESULT_OK){
                try {
                    //Start the activity to display the image
                    Intent intent = new Intent(getApplicationContext(), ImageSelectActivity.class);
                    intent.putExtra("IMAGE_DATA", imagePref.getString("currentImagePath", "none found"));
                    startActivity(intent);

                    //Log.d(TAG, "After Activity: " + imagePref.getString("currentImagePath", "none found"));
                } catch (Exception exception) {
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, exception.getMessage());
                }
            }else if(resultCode == RESULT_CANCELED){

                //Delete the temp file REMEMBER
                Log.d(TAG, "After Activity: " + imagePref.getString("currentImagePath", "none found"));
                deleteTempFiles(getExternalFilesDir(Environment.DIRECTORY_PICTURES));

            }
        }

        //After Gallery Activity Terminates
        if(requestCode == REQUEST_CODE_IMAGE_PICK){
            if(resultCode == RESULT_OK){
                try {

                    //Toast.makeText(this, "Image Selected!", Toast.LENGTH_SHORT).show();
                    Uri imageUri = (Uri) data.getData();
                    //pass the image to the Image Selection activity
                    Intent intent = new Intent(getApplicationContext(), PreviewImageFromGallery.class);
                    intent.putExtra("IMAGE_DATA", imageUri);
                    startActivity(intent);

                } catch (Exception exception) {
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, exception.getMessage());
                }
            }else if(resultCode == RESULT_CANCELED){

                //Delete the temp file REMEMBER

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }//onActivityResult

    private void deleteTempFiles(File file) {
        int file_size;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteTempFiles(f);
                        if(f.list().length == 0){
                            f.delete();
                        }
                    } else {
                        file_size = Integer.parseInt(String.valueOf(f.length()/1024));
                        if(file_size == 0){
                            f.delete();
                        }
                    }
                }
            }
        }
    }//deleteTempFiles
}