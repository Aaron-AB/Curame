package com.example.curameapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DiagnosisAdapter extends RecyclerView.Adapter<DiagnosisAdapter.DiagnosisViewHolder> {
    private String names[];
    private Float values[];
    private Context context;

    public DiagnosisAdapter(Context context, String names[], Float values[]){
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
        holder.itemName.setText(names[position]);
        holder.itemValue.setText(values[position].toString());
    }

    @Override
    public int getItemCount() {
        //This tells the adapter how many items its displaying
        return names.length;
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
