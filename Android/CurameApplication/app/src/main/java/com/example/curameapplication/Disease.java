package com.example.curameapplication;

import java.util.ArrayList;

public class Disease {
    public String name;
    public String description;
    public ArrayList<String> symptoms;
    public String treatment;
    public ArrayList<String> headings;

    //Constructor
    public Disease(String name, String description, ArrayList symptoms, String treatment) {
        this.name = name;
        this.description = description;
        this.symptoms = symptoms;
        this.treatment = treatment;
        headings =  new ArrayList<>();
        headings.add("Description");
        headings.add("Symptoms");
        headings.add("Treatment");
    }

    //Convert the information of a disease to an ArrayLists of ArrayLists
    public ArrayList<ArrayList<String>> toArrayLists() {
        //Convert the data to lists
        ArrayList<ArrayList<String>> diseaseInformation = new ArrayList<>();
        ArrayList<String> descList = new ArrayList<>();
        ArrayList<String> treatList = new ArrayList<>();
        descList.add(description);
        treatList.add(treatment);

        //Add them to the list of lists
        diseaseInformation.add(descList);
        diseaseInformation.add(symptoms);
        diseaseInformation.add(treatList);
        return diseaseInformation;
    }//toArrayLists

    //Get the headings of the information stored about the disease
    public ArrayList<String> getHeadings(){
        return headings;
    }

    //Returns the name of the disease
    public String getName() {
        return name;
    }//getName
}
