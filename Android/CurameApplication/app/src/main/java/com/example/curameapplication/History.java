package com.example.curameapplication;

import android.net.Uri;

import java.util.ArrayList;

public class History {

    private ArrayList<Uri> imageUris;
    private ArrayList<Prediction> predictions;

    //Constructor*
    public History(){
        imageUris = new ArrayList<Uri>();
        predictions = new ArrayList<Prediction>();
    }

    //This adds a history item to the history object
    public void addHistoryItem(Uri imageUri, Prediction prediction){
        imageUris.add(imageUri);
        predictions.add(prediction);
    }

    //This function returns all the images in history
    public ArrayList<Uri> getImages(){
        return imageUris;
    }

    //This function returns all the predictions in history
    public ArrayList<Prediction> getPredictions(){
        return predictions;
    }

    //This function returns the count
    public int  getHistoryItemCount(){
        return imageUris.size();
    }

}
