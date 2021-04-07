package com.example.curameapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class InformationActivity extends AppCompatActivity {

    private ImageView mainImage;
    private TextView diseaseTitle;
    private TextView percentage;
    private RecyclerView informationRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //Find out views
        mainImage = (ImageView)findViewById(R.id.mainImage);
        diseaseTitle = (TextView)findViewById(R.id.diseaseTitle);
        percentage = (TextView)findViewById(R.id.percentage);

        //**Test Data
        String imagePath = "/sdcard/Android/data/com.example.curameapplication/files/Pictures/SCAN_05_04_2021_03_13_15/scan.jpg";
        String diseaseName = "Disease Name";
        Float diseasePercentage = new Float(0.35);

        //Set image
        mainImage.setImageURI(Uri.fromFile(new File(imagePath)));

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
        item.add("Symptom 1 - You may experience this symptom");
        item.add("Symptom 2 - You may also experience this symptom");
        item.add("Symptom 3 - This is another symptom");
        content.add(item);

        item = new ArrayList<String>();
        item.add("Sorry. The Curame System does not have nay treatments for this illness available at the moment. Please contact a dermatologist for more information.");
        content.add(item);
        //**End test data

        //Display the information Adapter to the screen
        InformationAdapter adapter = new InformationAdapter(this, titles, content);
        informationRecycler.setAdapter(adapter);
        informationRecycler.setLayoutManager(new LinearLayoutManager(this));

        ScrollView mainScroll = findViewById(R.id.mainScroll);
        mainScroll.post(new Runnable() {public void run() {
            mainScroll.fullScroll(View.FOCUS_UP);
        }});
    }
}