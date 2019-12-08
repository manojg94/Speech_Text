package com.manoj.speech_text_demo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manoj.speech_text_demo.R;
import com.manoj.speech_text_demo.db.entity.Speech;

import java.util.ArrayList;
import java.util.List;


public class SpeechrecyclerViewAdapter extends RecyclerView.Adapter<SpeechrecyclerViewAdapter.viewHolder>
        implements Filterable {
    //model and interface
    public List<Speech> recyclerModel;
    private List<Speech> ListFiltered;

    //step 1
    public SpeechrecyclerViewAdapter(List<Speech> recyclerModel) {
        this.recyclerModel=recyclerModel;
        this.ListFiltered=recyclerModel;
    }



    ////step 2
    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       TextView names;
        Context context;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            //initializtions of the UI like textview etc

            context=itemView.getContext();
            names = itemView.findViewById(R.id.moviename);

        }

        @Override
        public void onClick(View view) {

         //  recyclerAdapter.onitemclick(view,getAdapterPosition());
        }
    }

    //below are the implementations of recycler view
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //create views here like layout initializtions only and retrurn the layout view
        //retrun viewholder with params as view and interface
        // .inflate 3rd parameter should be false
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.datalist,viewGroup,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, final int i) {
        //here  binding the data to the UI like textview etc.

        viewHolder.names.setText(ListFiltered.get(i).getSpeechText());
    }




    @Override
    public int getItemCount() {
        return ListFiltered.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    ListFiltered = recyclerModel;
                } else {
                    List<Speech> filteredList = new ArrayList<>();
                    for (Speech row : recyclerModel) {

                        // name match condition. this might differ depending on your requirement
                        if (row.getSpeechText().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    ListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = ListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ListFiltered = (ArrayList<Speech>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
