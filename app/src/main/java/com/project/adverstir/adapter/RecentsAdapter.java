package com.project.adverstir.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adverstir.R;
//import com.travel.travelmate.R;

import com.project.adverstir.RecyclerViewClickInterface;
//import com.travel.travelmate.RecyclerViewClickInterface;

import com.project.adverstir.model.RecentsData;
//import com.travel.travelmate.model.RecentsData;

import java.util.List;

public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.RecentsViewHolder> {

    Context context;
    List<RecentsData> recentsDataList;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    // Constructor
    public RecentsAdapter(Context context, List<RecentsData> recentsDataList, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.recentsDataList = recentsDataList;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public RecentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recents_row_items, parent, false);

        // here we create a recyclerview row item layout file
        return new RecentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentsViewHolder holder, int position) {

        holder.countryName.setText(recentsDataList.get(position).getCountryName());
        holder.placeName.setText(recentsDataList.get(position).getPlaceName());
        holder.price.setText(recentsDataList.get(position).getPrice());
        holder.placeImage.setImageResource(recentsDataList.get(position).getImageUrl());
        holder.starRating.setRating(recentsDataList.get(position).getStarRating());
    }

    @Override
    public int getItemCount() {
        return recentsDataList.size();
    }

    //public static final class RecentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    public class RecentsViewHolder extends RecyclerView.ViewHolder
    {
        ImageView placeImage;
        TextView placeName, countryName, price;
        RatingBar starRating;

        public RecentsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            placeImage = itemView.findViewById(R.id.location_image);
            placeName = itemView.findViewById(R.id.location_name);
            countryName = itemView.findViewById(R.id.country_name);
            price = itemView.findViewById(R.id.price);
            starRating = itemView.findViewById(R.id.star_rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}

