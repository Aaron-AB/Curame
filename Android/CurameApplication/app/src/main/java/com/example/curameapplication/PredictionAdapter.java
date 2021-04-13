package com.example.curameapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.PredictionViewHolder> {
    private ArrayList<String> names;
    private ArrayList<Float> values;
    private Context context;

    //Adapter Constructor
    public PredictionAdapter(Context context, Prediction prediction){
        //Get the prediction information
        Map<String, Float> predictionData = prediction.getPrediction();

        //Set the names and values of our prediction
        ArrayList<String> names = new ArrayList<String>(predictionData.keySet());
        ArrayList<Float> values = new ArrayList<Float>(predictionData.values());

        this.context = context;
        this.names = names;
        this.values = values;
    }


    /**
     * Display prediction items to view holder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public PredictionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Get the prediction View Layout and inflate it in the provided space for the adapter
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.prediction_item, parent, false);
        return new PredictionViewHolder(view);
    }//onCreateViewHolder


    /**
     * Display information in each of the history it
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull PredictionViewHolder holder, int position) {
        //This binds the items provided to the variables in the adapter
        holder.itemName.setText(names.get(position));
        //Set Percentage
        DecimalFormat df = new DecimalFormat("##");
        holder.itemValue.setText(df.format(values.get(position) * 100) + "%");

        //When the area is clicked, it displays the information for that item
        holder.informationArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InformationActivity.class);
                intent.putExtra("NAME_DATA", names.get(position));
                intent.putExtra("PERCENTAGE_DATA", (Float)values.get(position));
                //Get image data from parent activity
                intent.putExtra("SCAN_IMAGE", (Uri)((Activity) context).getIntent().getExtras().get("SCAN_IMAGE"));
                //Start the information activity
                context.startActivity(intent);
            }
        });
    }//onBindViewHolder


    /**
     * This tells the recycler adapter how many items to display
     * @return
     */
    @Override
    public int getItemCount() {
        //This tells the adapter how many items its displaying
        return names.size();
    }//getItemCount


    /**
     * This defines each prediction item view
     */
    public class PredictionViewHolder extends RecyclerView.ViewHolder{

        TextView itemName, itemValue;
        LinearLayout informationArea;

        public PredictionViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemValue = itemView.findViewById(R.id.itemValue);
            informationArea = itemView.findViewById(R.id.informationArea);
        }
    }//PredictionViewHolder
}
