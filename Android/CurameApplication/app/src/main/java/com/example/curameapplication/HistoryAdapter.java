package com.example.curameapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private ArrayList<Uri> imageUris;
    private ArrayList<Prediction> predictions;
    private Context context;

    //Adapter constructor
    public HistoryAdapter(Context context, History history){
        this.context = context;
        this.imageUris = history.getImages();
        this.predictions = history.getPredictions();
    }//HistoryAdapter

    //Display history items to view holder
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Get the History View Layout and inflate it in the provided space for the adapter
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.history_item, parent, false);
        return new HistoryAdapter.HistoryViewHolder(view);
    }//HistoryViewHolder

    //Display information in each of the history items
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        //Display the item date to history item
        holder.itemDate.setText("SCAN DATE: " + predictions.get(position).getDate());

        //Set image using picasso
        File imageFile = new File(imageUris.get(position).getPath());
        Picasso
                .get()
                .load(imageFile)
                .placeholder(R.drawable.empty)
                .fit()
                .centerCrop()
                .into(holder.itemImage);


        //get the predictions from the hash map
        Map<String, Float> prediction = predictions.get(position).getPrediction();
        Set<String> keys = prediction.keySet();

        //Set percentage
        String result = "";
        DecimalFormat df = new DecimalFormat("##");
        for(String key : keys){
            result = result + key + ": " + df.format(prediction.get(key) * 100) + "% chance.\n";
        }

        //Display the result of the prediction
        holder.itemPrediction.setText(result);

        //add onclick listener to view scan to go to the prediction select
        holder.historyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PredictionSelectActivity.class);
                intent.putExtra("PREDICTION_DATA", (Prediction)predictions.get(position));
                intent.putExtra("SCAN_IMAGE", (Uri)imageUris.get(position));
                //Start the Prediction Select activity
                context.startActivity(intent);
            }
        });

        //add onclick listen to trash history item
        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prompt the user if they are sure
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            //If the user selects yes
                            case DialogInterface.BUTTON_POSITIVE:
                                //Delete the selected item
                                File file = new File(imageUris.get(position).getPath());
                                deleteFile(file.getParentFile());
                                //Restart the activity
                                Intent intent = ((Activity) context).getIntent();
                                ((Activity) context).finish();
                                ((Activity) context).startActivity(intent);
                                break;
                        }
                    }
                };

                //Display the dialogue box
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this item?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }//onBindViewHolder

    //This function deletes an entire file directory
    private void deleteFile(File dir){
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteFile(f);
                    } else {
                        f.delete();
                    }
                }
            }
        }
        dir.delete();
    }//deleteFile

    //This tells the recycler adapter how many items to display
    @Override
    public int getItemCount() {
        return imageUris.size();
    }//getItemCount

    //This defines each history item view
    public class HistoryViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImage;
        TextView itemDate;
        TextView itemPrediction;
        View historyItem;
        ImageView trash;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.image);
            itemDate = itemView.findViewById(R.id.date);
            itemPrediction = itemView.findViewById(R.id.prediction);
            historyItem = itemView.findViewById(R.id.historyItem);
            trash = itemView.findViewById(R.id.trash);
        }//HistoryViewHolder
    }//HistoryViewHolder
}
