package com.example.eventified;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return name.length();
    }

    public class ClubViewHolder extends RecyclerView.ViewHolder{

        TextView nameDisplay, descDisplay;

        public ClubViewHolder(@NonNull View itemView) {
            super(itemView);
            nameDisplay = itemView.findViewById(R.id.nameText);
            descDisplay = itemView.findViewById(R.id.descText);
        }
    }
}
