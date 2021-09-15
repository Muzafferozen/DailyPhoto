package com.muzafferozen.dailyphoto;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muzafferozen.dailyphoto.databinding.RecyclerRowBinding;

import java.util.ArrayList;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.dailyHolder>{

    ArrayList<Daily> dailyArrayList;

    public DailyAdapter(ArrayList<Daily> dailyArrayList){

        this.dailyArrayList = dailyArrayList;
    }

    @NonNull
    @Override
    public dailyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new dailyHolder(recyclerRowBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull dailyHolder holder, int position) {

        holder.binding.recyclerViewTextView.setText(dailyArrayList.get(position).name);

    }

    @Override
    public int getItemCount() {



        return dailyArrayList.size();
    }

    public class dailyHolder extends RecyclerView.ViewHolder{

        private RecyclerRowBinding binding;


        public dailyHolder(RecyclerRowBinding binding) {


            super(binding.getRoot());

            this.binding = binding;
        }
    }



}
