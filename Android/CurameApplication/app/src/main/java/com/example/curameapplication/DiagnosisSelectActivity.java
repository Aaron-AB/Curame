package com.example.curameapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

public class DiagnosisSelectActivity extends AppCompatActivity {
    RecyclerView diagnosisRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_select);

        //Get data from Intent
        Prediction prediction = (Prediction)getIntent().getExtras().get("PREDICTION_DATA");
        Map<String, Float> predictionData = prediction.getPrediction();

        //Set the names and values of our prediction
        ArrayList<String> names = new ArrayList<String>(predictionData.keySet());
        ArrayList<Float> values = new ArrayList<Float>(predictionData.values());

        //Get the recycler for the view
        diagnosisRecycler = (RecyclerView)findViewById(R.id.diagnosisRecyler);

        //Initialize the adapter for the recycler
        DiagnosisAdapter adapter = new DiagnosisAdapter(this, names, values);
        diagnosisRecycler.setAdapter(adapter);
        diagnosisRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public void finishActivity(View view) {
        finish();
    }
}