package com.example.curameapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class DiagnosisSelectActivity extends AppCompatActivity {

    RecyclerView diagnosisRecycler;

    //Test Data
    String names[] = {"Name1", "Name2", "Name3", "Name4"};
    Float values[] = {new Float(0.30), new Float(0.45), new Float(0.42), new Float(0.40)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_select);

        //Get the recycler for the view
        diagnosisRecycler = (RecyclerView)findViewById(R.id.diagnosisRecyler);

        //Initialize the adapter for the recycler
        DiagnosisAdapter adapter = new DiagnosisAdapter(this, names, values);
        diagnosisRecycler.setAdapter(adapter);
        diagnosisRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}