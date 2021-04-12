package com.example.curameapplication;

import android.net.Uri;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    @Test
    void addHistoryItem() {
        History test_history = new History();

        Uri test_imageUri = Uri.parse("empty.png");

        Map<String, Float> valueMap = new HashMap<String, Float>();
        valueMap.put("name1", new Float(10.2));
        valueMap.put("name2", new Float(21.5));

        Prediction test_prediction = new Prediction(valueMap);

        test_history.addHistoryItem(test_imageUri, test_prediction);

        ArrayList<Uri> expected_uris = new ArrayList<>();
        expected_uris.add(test_imageUri);

        ArrayList<Prediction> expected_predictions = new ArrayList<>();
        expected_predictions.add(test_prediction);

        assertEquals(expected_uris, test_history.getImages());
        assertEquals(expected_predictions, test_history.getPredictions());
    }

    @Test
    void getImages() {
        History test_history = new History();

        Uri test_imageUri = Uri.parse("empty.png");

        Map<String, Float> valueMap = new HashMap<String, Float>();
        valueMap.put("name1", new Float(10.2));
        valueMap.put("name2", new Float(21.5));

        Prediction test_prediction = new Prediction(valueMap);

        test_history.addHistoryItem(test_imageUri, test_prediction);

        ArrayList<Uri> expected_uris = new ArrayList<>();
        expected_uris.add(test_imageUri);

        assertEquals(expected_uris, test_history.getImages());
    }

    @Test
    void getPredictions() {
        History test_history = new History();

        Uri test_imageUri = Uri.parse("empty.png");

        Map<String, Float> valueMap = new HashMap<String, Float>();
        valueMap.put("name1", new Float(10.2));
        valueMap.put("name2", new Float(21.5));

        Prediction test_prediction = new Prediction(valueMap);

        test_history.addHistoryItem(test_imageUri, test_prediction);

        ArrayList<Prediction> expected_predictions = new ArrayList<>();
        expected_predictions.add(test_prediction);

        assertEquals(expected_predictions, test_history.getPredictions());
    }

    @Test
    void getHistoryItemCount() {
        History test_history = new History();

        Uri test_imageUri = Uri.parse("empty.png");

        Map<String, Float> valueMap = new HashMap<String, Float>();
        valueMap.put("name1", new Float(10.2));
        valueMap.put("name2", new Float(21.5));

        Prediction test_prediction = new Prediction(valueMap);

        test_history.addHistoryItem(test_imageUri, test_prediction);

        assertEquals(1, test_history.getHistoryItemCount());
    }

}