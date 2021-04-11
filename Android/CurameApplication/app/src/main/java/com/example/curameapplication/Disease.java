package com.example.curameapplication;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Disease {
    public String name;
    public String description;
    public ArrayList<String> symptoms;
    public String treatment;

    public Disease(String name, String description, ArrayList symptoms, String treatment) {
        this.name = name;
        this.description = description;
        this.symptoms = symptoms;
        this.treatment = treatment;
    }

<<<<<<< HEAD
    public ArrayList<ArrayList<String>> toArrayLists() {
=======
    public ArrayList<ArrayList<String>> convToArrayList() {
>>>>>>> 04db1f0b84fae735f7d6d0de87484536649bb10f
        ArrayList<ArrayList<String>> diseaseInformation = new ArrayList<>();
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<String> descList = new ArrayList<>();
        ArrayList<String> treatList = new ArrayList<>();
        nameList.add(name);
        descList.add(description);
        treatList.add(treatment);

        diseaseInformation.add(nameList);
        diseaseInformation.add(descList);
        diseaseInformation.add(symptoms);
        diseaseInformation.add(treatList);
        return diseaseInformation;
    } // [[], [], [], []]

    public String toString() {
        return name + " " + description + " " + treatment;
    }

}
