package com.example.curameapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class HistoryActivity extends AppCompatActivity {
    private TextView message;
    private ArrayList<Uri> imageUris;
    private ArrayList<Prediction> predictions;
    private RecyclerView historyRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //test test view**
        message = (TextView) findViewById(R.id.message);

        //Initialize Directory Lists
        imageUris = new ArrayList<Uri>();
        predictions = new ArrayList<Prediction>();

        //Load the Scan History
        loadScanHistory();

        //Get the Recycler View;
        historyRecycler = (RecyclerView) findViewById(R.id.historyRecycler);

        //Render the history items
        if(imageUris.isEmpty()){
            historyRecycler.setVisibility(View.GONE);
            message.setText("No scans have been made yet.");
        }else{
            message.setVisibility(View.GONE);
            //Initialize the adapter for the recycler
            HistoryAdapter adapter = new HistoryAdapter(this, imageUris, predictions);
            historyRecycler.setAdapter(adapter);
            historyRecycler.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    //This function loads the directories for all images and prediction objects
    private void loadScanHistory(){
        //This is the directory where all the images and information are stored
        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files;
        File data_file;
        File scan_file;
        Uri image_uri;
        Prediction prediction_data;

        //Check if the picture directory exists
        if (file.isDirectory()) {
            //Get all the files in the picture directory
            files = file.listFiles();

            //Check if the picture directory has no files
            if (files != null) {
                //sort the array by most recent
                Arrays.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        if (o1.lastModified() > o2.lastModified()){
                            return -1;
                        }else if (o1.lastModified() == o2.lastModified()){
                            return 0;
                        }
                        return 1;
                    }
                });
                //For each file in the picture directory
                for (File f : files) {
                    //If that file is also a directory, i.e. is a scan history directory
                    if (f.isDirectory()) {
                        //Get the data.ser file
                        data_file = new File(f.getAbsolutePath() + "/data.ser");
                        //Get the scan.jpg file
                        scan_file = new File(f.getAbsolutePath() + "/scan.jpg");
                        //Check if BOTH files exists
                        if((data_file.exists()) && (scan_file.exists())){
                            //Set the image and data to null
                            image_uri = null;
                            prediction_data = null;

                            //Read and store the Prediction from from the data.ser and the image from scan.jpg
                            try{
                                FileInputStream fileIn = new FileInputStream(data_file);
                                ObjectInputStream in = new ObjectInputStream(fileIn);
                                prediction_data = (Prediction) in.readObject();
                                in.close();
                                fileIn.close();

                                //Read and store the uri form the scan.jpg
                                image_uri = Uri.parse(scan_file.toString());

                                //Add the image uri and the
                                predictions.add(prediction_data);
                                imageUris.add(image_uri);
                            } catch (IOException e) {
                                Log.d("IOException", e.getMessage());
                                return;
                            } catch (ClassNotFoundException e) {
                                Log.d("ClassNotFoundException", e.getMessage());
                                return;
                            }//Try Catch
                        }//If Both Files Exists
                    }//If the file is a Scan History Item Directory
                }//For all the files in the picture directory
            }//If the picture directory contains files.
        }//If the picture directory Exists
        return;
    }//loadScanHistory Function

    public void finishActivity(View view) {
        finish();
    }
}