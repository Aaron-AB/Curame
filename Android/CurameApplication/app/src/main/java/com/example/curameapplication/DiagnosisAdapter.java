package com.example.curameapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        DecimalFormat df = new DecimalFormat("##.###");
        holder.itemValue.setText(df.format(values.get(position) * 100) + "%");
    }

    @Override
    public int getItemCount() {
        //This tells the adapter how many items its displaying
        return names.size();
    }

    public class DiagnosisViewHolder extends RecyclerView.ViewHolder{

        TextView itemName, itemValue;

        public DiagnosisViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemValue = itemView.findViewById(R.id.itemValue);
        }
    }
}
