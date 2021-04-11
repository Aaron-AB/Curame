package com.example.curameapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class InformationActivity extends AppCompatActivity {

    private ImageView mainImage;
    private TextView diseaseTitle;
    private TextView percentage;
    private RecyclerView informationRecycler;

    private static final String TAG = MainActivity.class.getSimpleName();

    //Cloud Firestore instance
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Disease diseaseInfo;
    CollectionReference diseaseData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //Find out views
        mainImage = (ImageView)findViewById(R.id.mainImage);
        diseaseTitle = (TextView)findViewById(R.id.diseaseTitle);
        percentage = (TextView)findViewById(R.id.percentage);

        //**Test Data
        /*
        String imagePath = "/sdcard/Android/data/com.example.curameapplication/files/Pictures/SCAN_05_04_2021_03_13_15/scan.jpg";
        String diseaseName = "Disease Name";
        Float diseasePercentage = new Float(0.35);
         */

        Uri imageUri = (Uri)getIntent().getExtras().get("SCAN_IMAGE");
        String diseaseName = (String)getIntent().getStringExtra("NAME_DATA");
        Float diseasePercentage = (Float)getIntent().getExtras().get("PERCENTAGE_DATA");

        //Set image
        mainImage.setImageURI(imageUri);

        //Set disease title
        diseaseTitle.setText(diseaseName);

        //Set percentage
        DecimalFormat df = new DecimalFormat("##");
        percentage.setText(df.format(diseasePercentage * 100) + "%");

        //Initialize recycler view
        informationRecycler = (RecyclerView)findViewById(R.id.informationRecycler);

        //**Test Data
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();

        titles.add("Description");
        titles.add("Possible Symptoms");
        titles.add("Treatments");

        ArrayList<String> item;
        item = new ArrayList<String>();
        item.add("This is the section for the Description of the illness. Here you may write all about the illness");
        content.add(item);

        item = new ArrayList<String>();
        item.add("Symptom 1: You may experience this symptom");
        item.add("Symptom 2: You may also experience this symptom");
        item.add("Symptom 3: This is another symptom");
        content.add(item);

        item = new ArrayList<String>();
        item.add("Sorry. The Curame System does not have nay treatments for this illness available at the moment. Please contact a dermatologist for more information.");
        content.add(item);
        //**End test data

        //DATA FETCH FIREBASE

        Log.d("FIREBASE", "THIS IS THE START OF FIREBASE FETCH");
        fetchDisease(diseaseName, new Callback() {
            @Override
            public void onCallback(Disease disease) {
                diseaseInfo = disease;
                Log.d("CALLBACK WORKS", diseaseInfo.toString());
                //Add Logic here
            }
        });


        Log.d("FIREBASE", "THIS IS THE END OF FIREBASE FETCH");
        //END DATA FETCH
        //Display the information Adapter to the screen
        InformationAdapter adapter = new InformationAdapter(this, titles, content);
        informationRecycler.setAdapter(adapter);
        informationRecycler.setLayoutManager(new LinearLayoutManager(this));

        ScrollView mainScroll = findViewById(R.id.mainScroll);
        mainScroll.post(new Runnable() {public void run() {
            mainScroll.fullScroll(View.FOCUS_UP);
        }});
    }

    public void finishActivity(View view) {
        finish();
    }

    private void fetchDisease(String diseaseName, final Callback callback) {
        final ArrayList<Disease> diseaseDescLoc = new ArrayList<>();
        DocumentReference docRef = db.collection("Diseases").document(diseaseName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String name = document.getData().get("name").toString();

                        String description = document.getData().get("description").toString();

                        Object symptoms = document.getData().get("symptom");
                        ArrayList<String> symString = (ArrayList<String>) symptoms;

                        String treatment = document.getData().get("treatment").toString();

                        Disease disease = new Disease(name, description, symString, treatment);
                        callback.onCallback(disease);
                    } else {
                        Log.d(TAG, "No Document by that name exists");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

}