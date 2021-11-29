package com.example.eventified;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

public class ClubActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ImageView logo, banner;
    TextView name, description, location;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        String serverUrl = ""; //Inputs getClubInfo URL

        serverUrl += "clubName=" + query;

        recyclerView = findViewById(R.id.events_recycler);

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