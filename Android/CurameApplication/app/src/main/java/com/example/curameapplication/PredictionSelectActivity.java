package com.example.curameapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

public class PredictionSelectActivity extends AppCompatActivity {
    RecyclerView diagnosisRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_select);

        //Get data from Intent
        Prediction prediction = (Prediction)getIntent().getExtras().get("PREDICTION_DATA");

        //Get the recycler for the view
        diagnosisRecycler = (RecyclerView)findViewById(R.id.diagnosisRecyler);

        //Initialize the adapter for the recycler
        PredictionAdapter adapter = new PredictionAdapter(this, prediction);
        diagnosisRecycler.setAdapter(adapter);
        diagnosisRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    //Finish the activity
    public void finishActivity(View view) {
        finish();
    }//finishActivity
}