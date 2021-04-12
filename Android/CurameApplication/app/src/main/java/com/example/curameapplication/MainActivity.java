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
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //Request codes for all activities
    private static final int REQUEST_CODE_PERMISSIONS_CAMERA = 1;
    private static final int REQUEST_CODE_CAPTURE = 2;
    private static final int REQUEST_CODE_PERMISSIONS_GALLERY = 3;
    private static final int REQUEST_CODE_IMAGE_PICK = 4;
    private static final int REQUEST_CODE_IMAGE_PREVIEW = 5;

    //Tag for Log.d output
    private static final String TAG = "Debug";

    //Shared preferences
    private static final String PREFERENCES_NAME = "ImagePreferences";
    SharedPreferences imagePref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise Shared PreferencesEditor
        imagePref = getApplicationContext().getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        prefEditor = imagePref.edit();

    }//onCreate

    //THis function starts the Camera Activity
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

    //This function calls the gallery activity
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

    //This names and creates a file for an image taken or uploaded
    private File createImageFile() throws IOException {
        //Name the directory SCAN_dd_MM_yyyy_HH_mm_ss
        String dirName = "SCAN_"
                + new SimpleDateFormat(
                        "dd_MM_yyyy_HH_mm_ss", Locale.getDefault()
        ).format(new Date());

        //The directory would be in pictures
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + dirName);

        //Create an empty file called scan.jpg
        File image = new File(directory, "scan.jpg");

        //Return the image
        return image;
    }//createImageFile

    //This function calls the camera intent which starts the device camera and awaits the image
    private void callCaptureImageIntent(){
        //create an intent to start the camera activity
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //create an empty image file
        if(intent.resolveActivity(getPackageManager()) != null) {
            //Create an empty image file
            File image = null;
            try {
                image = createImageFile();
            } catch (IOException exception){
                Log.d("FILE ERROR", exception.getMessage());
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
                prefEditor.putString("currentImagePath", (String)image.getAbsolutePath());
                prefEditor.apply();

                //start the intent
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
    }//callGalleryIntent

    //This function checked the permissions before calling the camera and gallary activities
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

    //This function handles all activity results for finish intents.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //After Camera Activity Terminates
        if(requestCode == REQUEST_CODE_CAPTURE){
            if(resultCode == RESULT_OK){
                try {
                    //Start the classification process for images taken from camera
                    startClassification(imagePref.getString("currentImagePath", "none found"));
                } catch (Exception exception) {
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, exception.getMessage());
                }
            }else if(resultCode == RESULT_CANCELED){
                //Delete the canceled file
                try {
                    File file = new File(imagePref.getString("currentImagePath", "none found"));
                    file.delete();
                }catch (Exception exception){
                    Log.d(TAG, exception.getMessage());
                }
                //CLean up any other files and directories
                deleteTempFiles(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            }
        }

        //After Gallery Activity Terminates
        if(requestCode == REQUEST_CODE_IMAGE_PICK){
            if(resultCode == RESULT_OK){
                try {
                    //Toast.makeText(this, "Image Selected!", Toast.LENGTH_SHORT).show();
                    Uri imageUri = (Uri) data.getData();

                    //Ask the user to preview the selected image
                    Intent intent = new Intent(getApplicationContext(), PreviewImageFromGallery.class);
                    intent.putExtra("IMAGE_DATA", imageUri);
                    startActivityForResult(intent, REQUEST_CODE_IMAGE_PREVIEW);

                }catch (Exception exception) {
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, exception.getMessage());
                }
            }else if(resultCode == RESULT_CANCELED){

            }
        }

        //After Image Preview Terminates
        if(requestCode == REQUEST_CODE_IMAGE_PREVIEW){
            if(resultCode == RESULT_OK){//The user accepts the selected image
                try {
                    //get the URI from the preview if the image is accepted
                    Uri imageUri = (Uri) data.getParcelableExtra("IMAGE_URI");

                    //create a new image
                    Bitmap bitmap =  ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), imageUri));
                    //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    //Create an empty file
                    File image = null;
                    image = createImageFile();

                    //save the image
                    FileOutputStream outStream = new FileOutputStream(image);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.close();

                    //Place image path in cache storage
                    prefEditor.putString("currentImagePath", (String)image.getAbsolutePath());
                    prefEditor.apply();

                    //Start the classification process for images selected from gallery
                    startClassification(imagePref.getString("currentImagePath", "none found"));
                }
                catch (IOException exception){
                    Log.d(TAG, exception.getMessage());
                }
            }else if(resultCode == RESULT_CANCELED){//the user cancels the selected image
                //Prompt them to select again
                callGalleryIntent();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }//onActivityResult

    //This function cleans up any empty or unneeded files
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
                        file_size = Integer.parseInt(String.valueOf(f.length()));
                        if(file_size == 0){
                            f.delete();
                        }
                    }
                }
            }
        }
    }//deleteTempFiles

    //This function starts the classification
    private void startClassification(String imagePath){
        //Get the frame with the loading animation
        FrameLayout overlay = findViewById(R.id.overlayFrame);
        //Set the lading animation to visible
        overlay.setVisibility(View.VISIBLE);
        //create a thread that will handle classifying the image
        Activity currentActivity = this;
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                //Give the system time to display the loading animation
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Get the image from the path specified
                File imageFile = new File(imagePath);

                //Get the parent folder of the image
                String parentPath = imageFile.getParent();

                //Classify the image
                //Set the image to be classified
                ImageClassifier classifier = new ImageClassifier(currentActivity, imagePath);

                //Get the values from the classification
                Map<String, Float> valueMap = classifier.Classify();
                Log.d("CLASSIFICATION", valueMap.toString());

                //save the date and prediction in the same location as the image
                Prediction prediction = savePredictionInfo(valueMap, parentPath);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        //Start the display information Activity with the prediction made
                        Intent intent = new Intent(currentActivity, PredictionSelectActivity.class);
                        intent.putExtra("PREDICTION_DATA", prediction);
                        intent.putExtra("SCAN_IMAGE", Uri.parse(imagePath));
                        currentActivity.startActivity(intent);
                        //Turn off the processing screen
                        overlay.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }//startClassification

    //This function saves classification information as a prediction
    private Prediction savePredictionInfo(Map<String, Float> valueMap, String directory){
        Prediction prediction = new Prediction(valueMap);

        // write object to file
        try {
            //name the file data.ser
            FileOutputStream fos = new FileOutputStream(directory + "/data.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prediction);
            oos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        //Return the prediction
        return prediction;
    }//savePredictionInfo

    //Function to go to the Scan History Screen
    public void goToHistory(View view) {
        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
        startActivity(intent);
    }//goToHistory

    //go to about us page
    public void goToAboutUs(View view){
        Intent intent = new Intent(getApplicationContext(), AboutUsActivity.class);
        startActivity(intent);
    }//goToAboutUs

    //go to disclaimer page
    public void goToDisclaimer(View view) {
        Intent intent = new Intent(getApplicationContext(), DisclaimerActivity.class);
        startActivity(intent);
    }//goToDisclaimer
}