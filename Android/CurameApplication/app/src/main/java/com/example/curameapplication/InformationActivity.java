package com.example.curameapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.w3c.dom.Text;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class InformationActivity extends AppCompatActivity {

    private ImageView mainImage;
    private TextView diseaseTitle;
    private TextView percentage;
    private RecyclerView informationRecycler;
    private TextToSpeech textToSpeech;
    private FrameLayout imagePreview;
    private ImageView fullImage;
    private Uri imageUri;
    private CollectionReference diseaseData;
    private View informationContent;
    private View loadingContent;
    private TextView message;

    //Cloud Firestore instance
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Disease diseaseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //Find views
        mainImage = (ImageView)findViewById(R.id.mainImage);
        diseaseTitle = (TextView)findViewById(R.id.diseaseTitle);
        percentage = (TextView)findViewById(R.id.percentage);
        imagePreview = (FrameLayout)findViewById(R.id.imagePreview);
        fullImage = (ImageView)findViewById(R.id.fullImage);
        informationContent = findViewById(R.id.informationContent);
        loadingContent = findViewById(R.id.loadingContent);
        message = findViewById(R.id.message);

        //Get the name of the disease and the percentage chance
        String diseaseName = (String)getIntent().getStringExtra("NAME_DATA");
        Float diseasePercentage = (Float)getIntent().getExtras().get("PERCENTAGE_DATA");

        //Get the image of the disease being displayed
        imageUri = (Uri)getIntent().getExtras().get("SCAN_IMAGE");

        //Set image using picasso
        File imageFile = new File(imageUri.getPath());
        Picasso
                .get()
                .load(imageFile)
                .placeholder(R.drawable.empty)
                .fit()
                .centerInside()
                .into(mainImage);

        //Set disease title
        diseaseTitle.setText(diseaseName);

        //Set percentage
        DecimalFormat df = new DecimalFormat("##");
        percentage.setText(df.format(diseasePercentage * 100) + "%");

        //Initialize recycler view
        informationRecycler = (RecyclerView)findViewById(R.id.informationRecycler);

        //Initialize Text to speech
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        //Fetch and display the disease information
        fetchAndDisplayDisease(diseaseName);
    }

    //This function fetches the disease from Firebase, if the disease is found, it displays it, if not it displays a message
    private void fetchAndDisplayDisease(String diseaseName) {
        //This function fetches the named disease from the firebase db base
        DocumentReference docRef = db.collection("Diseases").document(diseaseName);

        //Create a listener to check when the document is completed loading
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            //Check if the document did not complete loading
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                //If the task is successful
                if (task.isSuccessful()) {
                    //get the result of the fetched document
                    DocumentSnapshot document = task.getResult();

                    //if there is data
                    if (document.exists()) {
                        //Get the disease information from the fetched document
                        Log.d("FETCH DEBUG", "DocumentSnapshot data: " + document.getData());
                        //Name
                        String name = document.getData().get("name").toString();
                        //Description
                        String description = document.getData().get("description").toString();
                        //Symptoms
                        Object symptoms = document.getData().get("symptom");
                        ArrayList<String> symString = (ArrayList<String>) symptoms;
                        //Treatment
                        String treatment = document.getData().get("treatment").toString();

                        //Create a disease object
                        Disease disease = new Disease(name, description, symString, treatment);

                        //Display the disease information
                        displayDiseaseInformation(disease);
                    } else {
                        //Display error message
                        Log.d("FETCH DEBUG", "No Document by that name exists");
                        message.setVisibility(View.VISIBLE);
                        loadingContent.setVisibility(View.GONE);
                        message.setText("No information is available for this skin disease.");
                    }

                } else {//This means the document did not complete loading

                    //Display error message
                    Log.d("FETCH DEBUG", "get failed with ", task.getException());
                    message.setVisibility(View.VISIBLE);
                    loadingContent.setVisibility(View.GONE);
                    message.setText("Failed to get information from server.");

                }
            }
        });
    } //fetchAndDisplayDisease

    //This function accepts a disease and displays it to the information recyclers
    private void displayDiseaseInformation(Disease disease) {
        //Hide the loading symbol and show the information
        loadingContent.setVisibility(View.GONE);
        informationContent.setVisibility(View.VISIBLE);

        //Show the information
        InformationAdapter adapter = new InformationAdapter(this, disease, textToSpeech);
        informationRecycler.setAdapter(adapter);
        informationRecycler.setLayoutManager(new LinearLayoutManager(this));
    }//displayDiseaseInformation

    public void finishActivity(View view) {
        finish();
    }

    //Clear text to speech on destroy
    @Override
    protected void onDestroy() {
        //Close the Text to Speech Library
        if(textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            Log.d("TTS CLEAR", "TTS Destroyed");
        }
        super.onDestroy();
    }//onDestroy

    //This function is used to show a large version of a disease
    public void showFull(View view) {
        imagePreview.setVisibility(View.VISIBLE);

        //Set image using picasso
        File imageFile = new File(imageUri.getPath());
        Picasso
                .get()
                .load(imageFile)
                .fit()
                .centerInside()
                .into(fullImage);
    }//showFull

    //This function hides the preview
    public void hideFull(View view) {
        imagePreview.setVisibility(View.GONE);
    }//hideFull

    //This function scrolls to top
    public void goToTop(View view) {
        ScrollView mainScroll = findViewById(R.id.mainScroll);
        mainScroll.post(new Runnable() {public void run() {
            mainScroll.fullScroll(View.FOCUS_UP);
        }});
    }//goToTop
}