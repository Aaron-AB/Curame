package com.example.curameapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private ArrayList<Uri> imageUris;
    private ArrayList<Prediction> predictions;
    private Context context;

    public HistoryAdapter(Context context, ArrayList<Uri> imageUris, ArrayList<Prediction> predictions){
        this.context = context;
        this.imageUris = imageUris;
        this.predictions = predictions;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Get the History View Layout and inflate it in the provided space for the adapter
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.history_item, parent, false);
        return new HistoryAdapter.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        //Display the item date to history item
        holder.itemDate.setText("SCAN DATE: " + predictions.get(position).getDate());
        /*
        //Display the item image to the history item
        holder.itemImage.setImageURI(imageUris.get(position));
         */
        File imageFile = new File(imageUris.get(position).getPath());
        //Set image using picasso
        Picasso
                .get()
                .load(imageFile)
                .placeholder(R.drawable.empty)
                .fit()
                .centerCrop()
                .into(holder.itemImage);

        String result = "";
        //get the predictions from the hash map
        Map<String, Float> prediction = predictions.get(position).getPrediction();
        Set<String> keys = prediction.keySet();
        //Set percentage
        DecimalFormat df = new DecimalFormat("##");
        for(String key : keys){
            result = result + key + ": " + df.format(prediction.get(key) * 100) + "%\n";
        }
        //Display the result of the prediction
        holder.itemPrediction.setText(result);

        //add onclick listener to view scan;
        holder.historyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DiagnosisSelectActivity.class);
                intent.putExtra("PREDICTION_DATA", (Prediction)predictions.get(position));
                intent.putExtra("SCAN_IMAGE", (Uri)imageUris.get(position));
                context.startActivity(intent);
            }
        });

        //add onclick listen to trash
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
    }

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
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

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
        }
    }
}
