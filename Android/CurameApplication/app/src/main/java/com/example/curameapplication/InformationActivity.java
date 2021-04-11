package com.example.curameapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        //Find out views
        mainImage = (ImageView)findViewById(R.id.mainImage);
        diseaseTitle = (TextView)findViewById(R.id.diseaseTitle);
        percentage = (TextView)findViewById(R.id.percentage);
        imagePreview = (FrameLayout)findViewById(R.id.imagePreview);
        fullImage = (ImageView)findViewById(R.id.fullImage);
        informationContent = findViewById(R.id.informationContent);
        loadingContent = findViewById(R.id.loadingContent);
        message = findViewById(R.id.message);

        imageUri = (Uri)getIntent().getExtras().get("SCAN_IMAGE");
        String diseaseName = (String)getIntent().getStringExtra("NAME_DATA");
        Float diseasePercentage = (Float)getIntent().getExtras().get("PERCENTAGE_DATA");

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
        fetchDisease(diseaseName);
    }

    private void fetchDisease(String diseaseName) {
        final ArrayList<Disease> diseaseDescLoc = new ArrayList<>();
        DocumentReference docRef = db.collection("Diseases").document(diseaseName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Create a disease object from the fetched information
                        Log.d("FETCH DEBUG", "DocumentSnapshot data: " + document.getData());
                        String name = document.getData().get("name").toString();
                        String description = document.getData().get("description").toString();
                        Object symptoms = document.getData().get("symptom");
                        ArrayList<String> symString = (ArrayList<String>) symptoms;
                        String treatment = document.getData().get("treatment").toString();
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
                } else {
                    //Display error message
                    Log.d("FETCH DEBUG", "get failed with ", task.getException());
                    message.setVisibility(View.VISIBLE);
                    loadingContent.setVisibility(View.GONE);
                    message.setText("Failed to get information from server.");
                }
            }
        });
    }

    //This function accepts a disease and displays it to the information recyclers
    private void displayDiseaseInformation(Disease disease) {
        //Hide the loading symbol and show the information
        loadingContent.setVisibility(View.GONE);
        informationContent.setVisibility(View.VISIBLE);

        //Show the information
        InformationAdapter adapter = new InformationAdapter(this, disease, textToSpeech);
        informationRecycler.setAdapter(adapter);
        informationRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

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
    }

    public void showFull(View view) {
        imagePreview.setVisibility(View.VISIBLE);
        File imageFile = new File(imageUri.getPath());
        Picasso
                .get()
                .load(imageFile)
                .fit()
                .centerInside()
                .into(fullImage);
    }

    public void hideFull(View view) {
        imagePreview.setVisibility(View.GONE);
    }

    //This function scrolls to top
    public void goToTop(View view) {
        ScrollView mainScroll = findViewById(R.id.mainScroll);
        mainScroll.post(new Runnable() {public void run() {
            mainScroll.fullScroll(View.FOCUS_UP);
        }});
    }
}