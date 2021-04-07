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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSIONS_CAMERA = 1;
    private static final int REQUEST_CODE_CAPTURE = 2;
    private static final int REQUEST_CODE_PERMISSIONS_GALLERY = 3;
    private static final int REQUEST_CODE_IMAGE_PICK = 4;
    private static final int REQUEST_CODE_IMAGE_PREVIEW = 5;
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
        //Name the directory SCAN_dd_MM_yyyy_HH_mm_ss
        String dirName = "SCAN_"
                + new SimpleDateFormat(
                        "dd_MM_yyyy_HH_mm_ss", Locale.getDefault()
        ).format(new Date());

        //Name the image IMG_dd_MM_yyyy_HH_mm_ss
        //Name the image scan
        String fileName = "scan";

        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + dirName);

        File image = new File(directory, "scan.jpg");

        /*
        File image = File.createTempFile(
                fileName,
                ".jpg",
                directory
        );
        */

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
                //Log.d(TAG, "After Activity: " + imagePref.getString("currentImagePath", "none found"));
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
                    //pass the image to the Image Selection activity
                    Intent intent = new Intent(getApplicationContext(), PreviewImageFromGallery.class);
                    intent.putExtra("IMAGE_DATA", imageUri);
                    startActivityForResult(intent, REQUEST_CODE_IMAGE_PREVIEW);

                } catch (Exception exception) {
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, exception.getMessage());
                }
            }else if(resultCode == RESULT_CANCELED){

            }
        }

        //After Image Preview Terminates
        if(requestCode == REQUEST_CODE_IMAGE_PREVIEW){
            if(resultCode == RESULT_OK){

                try {
                    //get the URI from the preview if the image is accepted
                    Uri imageUri = (Uri) data.getParcelableExtra("IMAGE_URI");
                    //Log.d("URI", imageUri.toString());

                    //create a new image
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    File image = null;
                    image = createImageFile();

                    //save the image
                    FileOutputStream outStream = new FileOutputStream(image);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, outStream);
                    outStream.close();

                    //save the date and prediction in the same location as the image
                    //Test Data
                    Map<String, Float> valueMap = new HashMap<String, Float>();
                    valueMap.put("Name1", new Float(0.30));
                    valueMap.put("Name2", new Float(0.35));
                    valueMap.put("Name3", new Float(0.40));
                    valueMap.put("Name4", new Float(0.45));
                    savePredictionInfo(valueMap, image.getParent());
                    //Save image path
                    currentImagePath = image.getAbsolutePath();
                    //Place image path in cache storage
                    prefEditor.putString("currentImagePath", (String)image.getAbsolutePath());
                    prefEditor.apply();

                    //Start the activity to display the image
                    Intent intent = new Intent(getApplicationContext(), ImageSelectActivity.class);
                    intent.putExtra("IMAGE_DATA", imagePref.getString("currentImagePath", "none found"));
                    startActivity(intent);
                }

                catch (IOException exception){
                    Log.d(TAG, exception.getMessage());
                }

            }else if(resultCode == RESULT_CANCELED){
                //Toast.makeText(this, "Canceled!", Toast.LENGTH_SHORT).show();
                callGalleryIntent();
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
                        //file_size = Integer.parseInt(String.valueOf(f.length()/1024));
                        file_size = Integer.parseInt(String.valueOf(f.length()));
                        if(file_size == 0){
                            f.delete();
                        }
                    }
                }
            }
        }
    }//deleteTempFiles

    private void savePredictionInfo(Map<String, Float> valueMap, String directory){
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
    }

    //TEST FUNCTION to go to selector screen
    public void goToSelector(View view) {
        Intent intent = new Intent(getApplicationContext(), DiagnosisSelectActivity.class);
        startActivity(intent);
    }

    //Function to go to the Scan History Screen
    public void goToHistory(View view) {
        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
        startActivity(intent);
    }

    //Function to go to the Information Screen
    public void goToInfo(View view) {
        Intent intent = new Intent(getApplicationContext(), InformationActivity.class);
        startActivity(intent);
    }
}