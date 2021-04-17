package com.example.curameapplication;

import android.app.Activity;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ImageClassifierTest {

    @Test
    void classify() {
        MainActivity activity = new MainActivity();

        String imageLocation = "vasc";  //Image of a vascular lesion
        ImageClassifier testClassifier = new ImageClassifier(activity, imageLocation);
        String expected = "Vascular Lesions";
        Float expFloat = 0f;
        Map<String, Float> testMap = testClassifier.Classify();

        //Test for correct classification
        String testKey = testMap.entrySet().stream().findFirst().get().getKey();
        assertEquals(expected, testKey);

        //Test for a classification % above 0
        Float testVal = testMap.entrySet().stream().findFirst().get().getValue();
        assertTrue(testVal > expFloat);
    }
}