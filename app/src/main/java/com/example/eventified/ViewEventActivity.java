package com.example.eventified;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Objects;

public class ViewEventActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ImageView logo;
    TextView name, description, location, nextDate, nextTime, title;
    Toolbar toolbar;

    String tempName, tempDesc, tempLocation, tempTitle, tempTime, tempDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        logo = findViewById(R.id.cClubLogo);
        name = findViewById(R.id.cClubName);
        title = findViewById(R.id.cTitle);
        description = findViewById(R.id.cDescription);
        toolbar = findViewById(R.id.toolbar);
        location = findViewById(R.id.cLocation);
        nextDate = findViewById(R.id.cDate);
        nextTime = findViewById(R.id.cTime);
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getData();
        setData();

        fetchClubRegistered(tempTitle);
    }

    private void getData(){

        if(getIntent().hasExtra("name") && getIntent().hasExtra("desc")&& getIntent().hasExtra("location")){

            tempName = getIntent().getStringExtra("name");
            tempTitle = getIntent().getStringExtra("title");
            tempDesc = getIntent().getStringExtra("desc");
            tempLocation = getIntent().getStringExtra("location");
            tempTime = getIntent().getStringExtra("time");
            tempDate = getIntent().getStringExtra("date");

        }else{
            Toast.makeText(this,"No Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        name.setText(tempName);
        toolbar.setTitle(tempTitle);
        description.setText(tempDesc);
        location.setText(tempLocation);
        nextTime.setText(tempTime);
        nextDate.setText(tempDate);
        title.setText(tempTitle);



        String logoUrl = "https://eventifiedbucketone.s3.amazonaws.com/logos/"+
                tempName.replace(' ', '+')+".png";

        Picasso.get().load(logoUrl).into(logo);
    }

    private void fetchClubRegistered(String title) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String serverUrl = sharedPreferences.getString("getEventRegistered", "");

        serverUrl += "title=" + title;

        recyclerView = findViewById(R.id.registered_list);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, serverUrl, null,

                response -> {
                    try {
                        JSONArray email = response.getJSONArray("email");
                        requestQueue.stop();

                        EmailListAdapter adapter = new EmailListAdapter(this, email);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    public void onPressDeleteClubEvent(View view) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String serverUrl = sharedPreferences.getString("putDeleteClubEvent", "");

        serverUrl += "title=" + title.getText();

        JsonObjectRequest stringRequest = new JsonObjectRequest (Request.Method.PUT, serverUrl, null,

                response -> {
                    Toast.makeText(this, "Event Deleted", Toast.LENGTH_LONG).show();
                    requestQueue.stop();
                    finish();
                },
                error -> {
                    Toast.makeText(this, "Error: something with deleting the event", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    requestQueue.stop();
                }) {
        };
        requestQueue.add(stringRequest);


    }
}