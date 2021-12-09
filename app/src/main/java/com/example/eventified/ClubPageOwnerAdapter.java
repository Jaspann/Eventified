package com.example.eventified;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class ClubPageOwnerAdapter extends RecyclerView.Adapter<ClubPageOwnerAdapter.EventViewHolder> {
    String name, defaultLocation;
    JSONArray title, desc, location, date, time, repeating;
    Context context;

    public ClubPageOwnerAdapter(Context context, JSONArray title, JSONArray desc,
                           JSONArray location, JSONArray date, JSONArray time,
                           JSONArray repeating, String name, String defaultLocation)
    {
        this.context = context;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.date = date;
        this.time = time;
        this.repeating = repeating;
        this.name = name;
        this.defaultLocation = defaultLocation;
    }

    @NonNull
    @Override
    public ClubPageOwnerAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.club_card, parent, false);
        return new ClubPageOwnerAdapter.EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubPageOwnerAdapter.EventViewHolder holder, int position) {
        try {
            holder.nameDisplay.setText(name);
            holder.titleDisplay.setText(title.getString(position));
            holder.descDisplay.setText(desc.getString(position));
            holder.dateDisplay.setText(date.getString(position).substring(5).replace('-', '/'));
            holder.actionText.setText(R.string.view);

            String disTime = time.getString(position).substring(0, time.getString(position).length() - 3);
            if(Integer.parseInt(disTime.substring(0, 2)) > 12)
            {
                disTime = Integer.parseInt(disTime.substring(0, 2)) - 12 + disTime.substring(2);
                disTime += " PM";
            }
            else if(Integer.parseInt(disTime.substring(0, 2)) == 12)
            {
                disTime += " PM";
            }
            else
            {
                disTime += " AM";
            }
            disTime = disTime.charAt(0) == '0' ? disTime.substring(1) : disTime;
            holder.timeDisplay.setText(disTime);


            if(location.getString(position) != "null") {
                holder.locationDisplay.setText(location.getString(position));
            }else{
                holder.locationDisplay.setText(defaultLocation);
            }

            String imageUrl = "https://eventifiedbucketone.s3.amazonaws.com/logos/"+
                    name.replace(' ', '+')+".png";

            Picasso.get().load(imageUrl).into(holder.clubLogo);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.button.setOnClickListener(v -> {

            Intent intent = new Intent(context, ViewEventActivity.class);
            intent.putExtra("title", holder.titleDisplay.getText());
            intent.putExtra("name", holder.nameDisplay.getText());
            intent.putExtra("location", holder.locationDisplay.getText());
            intent.putExtra("desc", holder.descDisplay.getText());
            intent.putExtra("time", holder.timeDisplay.getText());
            intent.putExtra("date", holder.dateDisplay.getText());

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return title.length();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{

        TextView nameDisplay, descDisplay, titleDisplay, locationDisplay,
                dateDisplay, timeDisplay, actionText;
        ImageView clubLogo;
        Button button;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            nameDisplay = itemView.findViewById(R.id.cClubName);
            descDisplay = itemView.findViewById(R.id.cDescription);
            clubLogo = itemView.findViewById(R.id.cClubLogo);
            titleDisplay = itemView.findViewById(R.id.cTitle);
            locationDisplay = itemView.findViewById(R.id.cLocation);
            dateDisplay = itemView.findViewById(R.id.cDate);
            timeDisplay = itemView.findViewById(R.id.cTime);
            button = itemView.findViewById(R.id.register_button);
            actionText = itemView.findViewById(R.id.action_button_text);

        }
    }


}
