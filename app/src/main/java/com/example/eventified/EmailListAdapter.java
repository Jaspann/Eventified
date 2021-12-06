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

public class EmailListAdapter extends RecyclerView.Adapter<EmailListAdapter.EmailViewHolder>{

    JSONArray email;
    Context context;

    public EmailListAdapter(Context ct, JSONArray email)
    {
        context = ct;
        this.email = email;
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.email_card, parent, false);
        return new EmailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        try {
            holder.emailDisplay.setText(email.getString(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return email.length();
    }

    public class EmailViewHolder extends RecyclerView.ViewHolder {
        TextView emailDisplay;
        ConstraintLayout mainLayout;

        public EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            emailDisplay = itemView.findViewById(R.id.descText);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
}
