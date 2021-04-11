package com.example.curameapplication;

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

    public ArrayList<ArrayList<String>> toArrayLists() {
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
