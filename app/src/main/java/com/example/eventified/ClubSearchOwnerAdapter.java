package com.example.eventified;

import android.content.Context;
import android.content.Intent;
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

public class ClubSearchOwnerAdapter extends RecyclerView.Adapter<ClubSearchOwnerAdapter.ClubViewHolder> {

    JSONArray name, desc, location;
    Context context;

    public ClubSearchOwnerAdapter(Context ct, JSONArray nm, JSONArray ds, JSONArray loc)
    {
        context = ct;
        name = nm;
        desc = ds;
        location = loc;
    }

    @NonNull
    @Override
    public ClubSearchOwnerAdapter.ClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.club_search_card, parent, false);
        return new ClubSearchOwnerAdapter.ClubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubSearchOwnerAdapter.ClubViewHolder holder, int position) {
        try {

            holder.nameDisplay.setText(name.getString(position));
            holder.descDisplay.setText(desc.getString(position));
            holder.location = location.getString(position);

            String imageUrl = "https://eventifiedbucketone.s3.amazonaws.com/logos/"+
                    name.getString(position).replace(' ', '+')+".png";

            Picasso.get().load(imageUrl).into(holder.clubLogo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.mainLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, ClubOwnerActivity.class);

            try {
                intent.putExtra("name", name.getString(holder.getAdapterPosition()));
                intent.putExtra("description", desc.getString(holder.getAdapterPosition()));
                intent.putExtra("location", location.getString(holder.getAdapterPosition()));
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
        String location;

        public ClubViewHolder(@NonNull View itemView) {
            super(itemView);
            nameDisplay = itemView.findViewById(R.id.nameText);
            descDisplay = itemView.findViewById(R.id.descText);
            clubLogo = itemView.findViewById(R.id.clubLogo);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
}
