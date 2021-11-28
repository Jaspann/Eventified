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

public class ClubSearchAdapter extends RecyclerView.Adapter<ClubSearchAdapter.ClubViewHolder> {

    JSONArray name, desc;
    Context context;

    public ClubSearchAdapter(Context ct, JSONArray nm, JSONArray ds)
    {
        context = ct;
        name = nm;
        desc = ds;
    }

    @NonNull
    @Override
    public ClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.club_search_card, parent, false);
        return new ClubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubViewHolder holder, int position) {
        try {
            holder.nameDisplay.setText(name.getString(position));
            holder.descDisplay.setText(desc.getString(position));

            String imageUrl = "https://eventifiedbucketone.s3.amazonaws.com/logos/"+
                    name.getString(position).replace(' ', '+')+".png";

            Picasso.get().load(imageUrl).into(holder.clubLogo);

            //Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
            //InputStream is = (InputStream) new URL(imageUrl).getContent();
            //holder.clubLogo.setImageBitmap(bitmap);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.mainLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, ClubActivity.class);

            try {
                intent.putExtra("name", name.getString(holder.getAdapterPosition()));
                intent.putExtra("description", desc.getString(holder.getAdapterPosition()));
                context.startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.length();
    }

    public class ClubViewHolder extends RecyclerView.ViewHolder{

        TextView nameDisplay, descDisplay;
        ImageView clubLogo;
        ConstraintLayout mainLayout;

        public ClubViewHolder(@NonNull View itemView) {
            super(itemView);
            nameDisplay = itemView.findViewById(R.id.nameText);
            descDisplay = itemView.findViewById(R.id.descText);
            clubLogo = itemView.findViewById(R.id.clubLogo);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
}
