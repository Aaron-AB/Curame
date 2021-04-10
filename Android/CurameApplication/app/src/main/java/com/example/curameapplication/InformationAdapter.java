package com.example.curameapplication;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder>{

    private ArrayList<String> sectionTitle;
    private ArrayList<ArrayList<String>> sectionContent;
    private Context context;
    private TextToSpeech textToSpeech;

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

        //Click to read function
        //Create a textToSpeech engine
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        //Set the on click listener
        holder.informationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the text to read
                String text = holder.content.getText().toString();
                //Convert to speech
                int speech = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sectionTitle.size();
    }

    public class InformationViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView content;
        private View informationItem;

        public InformationViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.heading);
            content = itemView.findViewById(R.id.textBody);
            informationItem = itemView.findViewById(R.id.informationItem);
        }
    }
}
