package com.example.curameapplication;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PredictionTest {

    @Test
    public void testGetDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String expected_date = dateFormat.format(calendar.getTime());

        Map<String, Float> valueMap = new HashMap<String, Float>();
        valueMap.put("name1", new Float(10.2));
        valueMap.put("name2", new Float(21.5));

        Prediction test_prediction = new Prediction(valueMap);

        assertEquals(expected_date, test_prediction.getDate());
    }

    @Test
    public void testGetPrediction() {
        Map<String, Float> valueMap = new HashMap<String, Float>();
        valueMap.put("name1", new Float(10.2));
        valueMap.put("name2", new Float(21.5));

        Prediction test_prediction = new Prediction(valueMap);

        assertEquals(valueMap, test_prediction.getPrediction());
    }

    @Test
    public void testEquals() {
        Map<String, Float> valueMap = new HashMap<String, Float>();
        valueMap.put("name1", new Float(10.2));
        valueMap.put("name2", new Float(21.5));

        Prediction test_prediction1 = new Prediction(valueMap);
        Prediction test_prediction2 = new Prediction(valueMap);

        Map<String, Float> valueMap2 = new HashMap<String, Float>();
        valueMap.put("name1", new Float(10.5));
        valueMap.put("name2", new Float(20.1));

        Prediction test_prediction3 = new Prediction(valueMap2);

        assertTrue(test_prediction1.equals(test_prediction2));
        assertFalse(test_prediction1.equals(test_prediction3));
    }
}