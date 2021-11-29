package com.example.eventified;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ClubPageAdapter extends RecyclerView.Adapter<ClubPageAdapter.EventViewHolder> {

    String name;
    JSONArray title, desc, location, date, time, repeating;
    Context context;

    public ClubPageAdapter(Context context, JSONArray title, JSONArray desc,
                           JSONArray location, JSONArray date, JSONArray time,
                           JSONArray repeating, String name)
    {
        this.context = context;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.date = date;
        this.time = time;
        this.repeating = repeating;
        this.name = name;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.club_card, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        try {
            holder.nameDisplay.setText(name);
            holder.titleDisplay.setText(title.getString(position));
            holder.descDisplay.setText(desc.getString(position));
            holder.dateDisplay.setText(date.getString(position));
            holder.timeDisplay.setText(time.getString(position));
            holder.locationDisplay.setText(location.getString(position));

            String imageUrl = "https://eventifiedbucketone.s3.amazonaws.com/logos/"+
                    name.replace(' ', '+')+".png";

            Picasso.get().load(imageUrl).into(holder.clubLogo);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return title.length();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{

        TextView nameDisplay, descDisplay, titleDisplay, locationDisplay, dateDisplay, timeDisplay;
        ImageView clubLogo;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            nameDisplay = itemView.findViewById(R.id.cClubName);
            descDisplay = itemView.findViewById(R.id.cDescription);
            clubLogo = itemView.findViewById(R.id.cClubLogo);
            titleDisplay = itemView.findViewById(R.id.cTitle);
            locationDisplay = itemView.findViewById(R.id.cLocation);
            dateDisplay = itemView.findViewById(R.id.cDate);
            timeDisplay = itemView.findViewById(R.id.cTime);

        }
    }


}
