package com.example.curameapplication;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Prediction implements Serializable {

    private String date;
    private Map<String, Float> predictionValueMap;

    public Prediction(Map<String, Float> valueMap){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        predictionValueMap = valueMap;
    }

    public String getDate(){
        return date;
    }

    public Map<String, Float> getPrediction(){
        return predictionValueMap;
    }
}
