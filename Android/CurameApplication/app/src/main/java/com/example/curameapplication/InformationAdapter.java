package com.example.curameapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder>{

    private ArrayList<String> sectionTitle;
    private ArrayList<ArrayList<String>> sectionContent;
    private Context context;

    public InformationAdapter(Context context, ArrayList<String> sectionTitle, ArrayList<ArrayList<String>> sectionContent){
        this.sectionTitle = sectionTitle;
        this.sectionContent = sectionContent;
        this.context = context;
    }

    @NonNull
    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Get the Information View Layout and inflate it in the provided space for the adapter
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.information_item, parent, false);
        return new InformationAdapter.InformationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationViewHolder holder, int position) {
        holder.title.setText(sectionTitle.get(position));

        ArrayList<String> content = sectionContent.get(position);
        if(content.size() == 1){
            holder.content.setText(content.get(0));
        }else if(content.size()>1){
            String str = "";
            boolean first = false;
            for(String item : content){
                if(!first)
                    first = true;
                else
                    str = str + "\n";
                str = str + item;
            }
            holder.content.setText(str);
        }else{
            holder.content.setText("None available.");
        }
    }

    @Override
    public int getItemCount() {
        return sectionTitle.size();
    }

    public class InformationViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView content;

        public InformationViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.heading);
            content = itemView.findViewById(R.id.textBody);
        }
    }
}
