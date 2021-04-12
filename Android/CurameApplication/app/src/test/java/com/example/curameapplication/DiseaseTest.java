package com.example.curameapplication;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DiseaseTest {

    @Test
    public void testToArrayLists() {
        ArrayList<String> sym = new ArrayList<>();
        sym.add("test_symptom_1");
        sym.add("test_symptom_2");
        Disease test_disease = new Disease("test_name", "test_description", sym, "test_treatment");

        ArrayList<ArrayList<String>> expected_output = new ArrayList<ArrayList<String>>();
        ArrayList<String> description = new ArrayList<String>();
        description.add("test_description");
        ArrayList<String> treatment = new ArrayList<String>();
        treatment.add("test_treatment");
        expected_output.add(description);
        expected_output.add(sym);
        expected_output.add(treatment);

        assertEquals(expected_output, test_disease.toArrayLists());
    }

    @Test
    public void testGetHeadings() {
        ArrayList<String> sym = new ArrayList<>();
        sym.add("test_symptom_1");
        sym.add("test_symptom_2");
        Disease test_disease = new Disease("test_name", "test_description", sym, "test_treatment");
        ArrayList<String> expected_headings =  new ArrayList<>();
        expected_headings.add("Description");
        expected_headings.add("Symptoms");
        expected_headings.add("Treatment");

        assertEquals(expected_headings, test_disease.getHeadings());
    }

    @Test
    public void testGetName() {
        ArrayList<String> sym = new ArrayList<>();
        sym.add("test_symptom_1");
        sym.add("test_symptom_2");
        Disease test_disease = new Disease("test_name", "test_description", sym, "test_treatment");

        assertEquals("test_name", test_disease.getName());
    }
}