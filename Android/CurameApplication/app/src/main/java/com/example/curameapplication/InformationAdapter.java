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

    //Adapter Constructor
    public InformationAdapter(Context context, Disease disease, TextToSpeech tts){
        //Get the headings for the information from the disease
        this.sectionTitle = disease.getHeadings();

        //Get the information from the disease
        this.sectionContent = disease.toArrayLists();
        this.context = context;
        this.textToSpeech = tts;
    }

    //Display disease information items to view holder
    @NonNull
    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Get the Information View Layout and inflate it in the provided space for the adapter
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.information_item, parent, false);
        return new InformationAdapter.InformationViewHolder(view);
    }

    //Display content in each of the information items
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

        //Set click to read function
        holder.informationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the text to read
                String text = holder.content.getText().toString();

                //Fix the pronunciation of Curame
                text.replace("Curame", "Q ra may");

                //Convert to speech
                int speech = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    //This tells the recycler how many items it is displaying
    @Override
    public int getItemCount() {
        return sectionTitle.size();
    }

    //This defines each information item view
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
