package com.example.eventified;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter {

    JSONArray title, desc, location, date, time, repeating, personal, name;
    Context context;

    public HomeAdapter(Context context, JSONArray title, JSONArray desc,
                           JSONArray location, JSONArray date, JSONArray time,
                           JSONArray repeating, JSONArray personal, JSONArray name)
    {
        this.context = context;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.date = date;
        this.time = time;
        this.repeating = repeating;
        this.personal = personal;
        this.name = name;
    }

    @Override
    public int getItemViewType(int position)
    {
        try {
            if(personal.getInt(position) == 0) {
                return 0;
            }
            else
            {
                return 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(viewType == 0) {
            view = inflater.inflate(R.layout.club_card, parent, false);
            return new ClubEventViewHolder(view);
        }
        else
        {
            view = inflater.inflate(R.layout.event_card, parent, false);
            return new PersonalEventViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            if(personal.getInt(position) == 1)
            {
                ((PersonalEventViewHolder)holder).userDate = date.getString(position);
                ((PersonalEventViewHolder)holder).userTime = time.getString(position);
                ((PersonalEventViewHolder)holder).descText.setText(title.getString(position));
                ((PersonalEventViewHolder)holder).dateText.setText(date.getString(position).substring(5).replace('-', '/'));
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
                ((PersonalEventViewHolder)holder).timeText.setText(disTime);

                ((PersonalEventViewHolder)holder).button.setOnClickListener(v -> {

                    RequestQueue requestQueue = Volley.newRequestQueue(context);

                    SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                    String email = sharedPreferences.getString("email", "");

                    String serverUrl = sharedPreferences.getString("putDeleteUserEvent", "");

                    serverUrl += "title=" + ((PersonalEventViewHolder)holder).descText.getText();
                    serverUrl += "&time=" + ((PersonalEventViewHolder)holder).userTime;
                    serverUrl += "&date=" + ((PersonalEventViewHolder)holder).userDate;
                    serverUrl += "&email=" + email;

                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, serverUrl, null,

                            response -> {
                                Toast.makeText(context, "The event has been removed", Toast.LENGTH_LONG).show();
                                requestQueue.stop();
                            },
                            error -> {
                                Toast.makeText(context, "Error: something with removing the event went wrong", Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                                requestQueue.stop();
                            }) {
                    };
                    requestQueue.add(stringRequest);
                });
            }
            else
            {
                ((ClubEventViewHolder)holder).titleDisplay.setText(title.getString(position));
                ((ClubEventViewHolder)holder).descDisplay.setText(desc.getString(position));
                ((ClubEventViewHolder)holder).nameDisplay.setText(name.getString(position));
                ((ClubEventViewHolder)holder).locationDisplay.setText(location.getString(position));
                ((ClubEventViewHolder)holder).dateDisplay.setText(date.getString(position).substring(5).replace('-', '/'));

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
                ((ClubEventViewHolder)holder).timeDisplay.setText(disTime);

                String imageUrl = "https://eventifiedbucketone.s3.amazonaws.com/logos/"+
                        name.getString(position).replace(' ', '+')+".png";

                Picasso.get().load(imageUrl).into(((ClubEventViewHolder)holder).clubLogo);


                ((ClubEventViewHolder)holder).button.setVisibility(View.GONE);
                ((ClubEventViewHolder)holder).actionText.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return title.length();
    }

    class ClubEventViewHolder
            extends RecyclerView.ViewHolder {

        TextView nameDisplay, descDisplay, titleDisplay,
                locationDisplay, dateDisplay, timeDisplay, actionText;
        ImageView clubLogo;
        Button button;

        public ClubEventViewHolder(@NonNull View itemView) {
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

    class PersonalEventViewHolder
            extends RecyclerView.ViewHolder {

        TextView timeText, descText, dateText;
        String userDate, userTime;
        ImageButton button;

        public PersonalEventViewHolder(@NonNull View itemView)
        {
            super(itemView);
            timeText = itemView.findViewById(R.id.timeText);
            descText = itemView.findViewById(R.id.descText);
            dateText = itemView.findViewById(R.id.dateText);
            button = itemView.findViewById(R.id.deleteEventButton);
        }
    }
}
