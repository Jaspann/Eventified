package com.example.eventified;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Objects;

public class ClubActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ImageView logo, banner;
    TextView name, description, location, nextDate, nextTime;
    Toolbar toolbar;

    String tempName, tempDesc, tempLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        logo = findViewById(R.id.club_logo);
        banner = findViewById(R.id.bannerImageView);
        name = findViewById(R.id.club_name);
        description = findViewById(R.id.club_description);
        toolbar = findViewById(R.id.toolbar);
        location = findViewById(R.id.club_location);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getData();
        setData();

        fetchClubInfo(tempName);

    }

    private void getData(){

        if(getIntent().hasExtra("name") && getIntent().hasExtra("description")&& getIntent().hasExtra("location")){

            tempName = getIntent().getStringExtra("name");
            tempDesc = getIntent().getStringExtra("description");
            tempLocation = getIntent().getStringExtra("location");

        }else{
            Toast.makeText(this,"No Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        name.setText(tempName);
        toolbar.setTitle(tempName);
        description.setText(tempDesc);
        location.setText(tempLocation);


        String logoUrl = "https://eventifiedbucketone.s3.amazonaws.com/logos/"+
                tempName.replace(' ', '+')+".png";

        Picasso.get().load(logoUrl).into(logo);

        String bannerUrl = "https://eventifiedbucketone.s3.amazonaws.com/banners/"+
                tempName.replace(' ', '+')+".jpg";

        Picasso.get().load(bannerUrl).into(banner);

    }

    public void fetchClubInfo(String query)
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String serverUrl = sharedPreferences.getString("getClubInfo", "");

        serverUrl += "clubName=" + query;

        recyclerView = findViewById(R.id.events_recycler);
        nextDate = findViewById(R.id.next_meeting_date);
        nextTime = findViewById(R.id.next_meeting_time);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, serverUrl, null,

                response -> {
                    try {
                        JSONArray titles = response.getJSONArray("title");
                        JSONArray descriptions = response.getJSONArray("desc");
                        JSONArray locations = response.getJSONArray("location");
                        JSONArray dates = response.getJSONArray("date");
                        JSONArray times = response.getJSONArray("time");
                        JSONArray repeating = response.getJSONArray("repeating");
                        requestQueue.stop();

                        ClubPageAdapter adapter = new ClubPageAdapter(this,
                                titles, descriptions, locations, dates, times, repeating, query, tempLocation);

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));

                        nextDate.setText(dates.getString(0).substring(5).replace('-', '/'));

                        String disTime = times.getString(0).substring(0, times.getString(0).length() - 3);
                        if(Integer.parseInt(disTime.substring(0, 2)) > 12)
                        {
                            disTime = Integer.parseInt(disTime.substring(0, 2)) - 12 + disTime.substring(2);
                            disTime += " PM";
                        }
                        else
                        {
                            disTime += " AM";
                        }

                        nextTime.setText(disTime);

                    } catch (JSONException e) {
                        Toast.makeText(this, "Something with the request is wrong, or there are no events for this club", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        requestQueue.stop();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error: something with Volley is wrong", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    requestQueue.stop();
                }) {
        };
        requestQueue.add(stringRequest);
    }

    public void onPressJoin(View view) {

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        String serverUrl = sharedPreferences.getString("putStudentClub", "");

        serverUrl += "email=" + email + "&clubName=" + name.getText().toString();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, serverUrl, null,

                response -> {
                    try {
                        boolean added = response.getBoolean("added");
                        requestQueue.stop();
                        if(added)
                        {
                            Toast.makeText(this, "You have joined the club!", Toast.LENGTH_LONG).show();
                        }
                        if(!added)
                        {
                            Toast.makeText(this, "You have left the club.", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error: something with the request is wrong", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        requestQueue.stop();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error: something with Volley is wrong", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    requestQueue.stop();
                }) {
        };
        requestQueue.add(stringRequest);

    }
}