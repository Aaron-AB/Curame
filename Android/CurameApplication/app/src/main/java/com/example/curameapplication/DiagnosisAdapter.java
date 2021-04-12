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

public class DiagnosisAdapter extends RecyclerView.Adapter<DiagnosisAdapter.DiagnosisViewHolder> {
    private ArrayList<String> names;
    private ArrayList<Float> values;
    private Context context;

    public DiagnosisAdapter(Context context, ArrayList<String> names, ArrayList<Float> values){
        this.context = context;
        this.names = names;
        this.values = values;
    }

    @NonNull
    @Override
    public DiagnosisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Get the Diagnosis View Layout and inflate it in the provided space for the adapter
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.diagnosis_item, parent, false);
        return new DiagnosisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiagnosisViewHolder holder, int position) {
        //This binds the items provided to the variables in the adapter
        holder.itemName.setText(names.get(position));
        //Set Percentage
        DecimalFormat df = new DecimalFormat("##");
        holder.itemValue.setText(df.format(values.get(position) * 100) + "%");
        holder.informationArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InformationActivity.class);
                intent.putExtra("NAME_DATA", names.get(position));
                intent.putExtra("PERCENTAGE_DATA", (Float)values.get(position));
                //Get image data from parent activity
                intent.putExtra("SCAN_IMAGE", (Uri)((Activity) context).getIntent().getExtras().get("SCAN_IMAGE"));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //This tells the adapter how many items its displaying
        return names.size();
    }

    public class DiagnosisViewHolder extends RecyclerView.ViewHolder{

        TextView itemName, itemValue;
        LinearLayout informationArea;

        public DiagnosisViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemValue = itemView.findViewById(R.id.itemValue);
            informationArea = itemView.findViewById(R.id.informationArea);
        }
    }
}
