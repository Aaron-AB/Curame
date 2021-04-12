package com.example.curameapplication;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Prediction implements Serializable {

    private String date;
    private Map<String, Float> predictionValueMap;

    //Constructor
    public Prediction(Map<String, Float> valueMap){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        predictionValueMap = valueMap;
    }

    //Returns the date the prediction was made
    public String getDate(){
        return date;
    }

    //Returns the prediction made
    public Map<String, Float> getPrediction(){
        return predictionValueMap;
    }

    //equals method

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Prediction)) {
            return false;
        }

        Prediction pred = (Prediction)obj;

        return((this.getDate().equals(pred.getDate())) && (this.getPrediction().equals(pred.getPrediction())));
    }
}
