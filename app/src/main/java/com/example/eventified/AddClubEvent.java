package com.example.eventified;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class AddClubEvent extends AppCompatActivity {

    EditText title, location, desc, time, date;
    ImageView logo;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_club_event);
        name = getIntent().getStringExtra("name");
        logo = findViewById(R.id.club_logo);

        String logoUrl = "https://eventifiedbucketone.s3.amazonaws.com/logos/"+
                name.replace(' ', '+')+".png";

        Picasso.get().load(logoUrl).into(logo);

    }

    public void onPressAddEvent(View view) {


        title = findViewById(R.id.edit_title);
        location = findViewById(R.id.edit_location);
        desc = findViewById(R.id.edit_description);
        time = findViewById(R.id.edit_time);
        date = findViewById(R.id.edit_date);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String serverUrl = sharedPreferences.getString("putAddClubEvent", "");

        serverUrl += "title=" + title.getText();
        serverUrl += "&location=" + location.getText();
        serverUrl += "&desc=" + desc.getText();
        serverUrl += "&date=" + date.getText();
        serverUrl += "&time=" + time.getText();
        serverUrl += "&name=" + name;


        JsonObjectRequest stringRequest = new JsonObjectRequest (Request.Method.PUT, serverUrl, null,

                response -> {
                    Toast.makeText(this, "Event Added", Toast.LENGTH_LONG).show();
                    requestQueue.stop();
                    finish();
                },
                error -> {
                    Toast.makeText(this, "Error: something with adding the event", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    requestQueue.stop();
                }) {
        };
        requestQueue.add(stringRequest);

    }
}