package com.example.eventified;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class AddUserEvent extends AppCompatActivity {

    EditText title, time, date;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_event);
    }

    public void onPressAddEvent(View view) {

        title = findViewById(R.id.edit_title);
        time = findViewById(R.id.edit_time);
        date = findViewById(R.id.edit_date);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String serverUrl = sharedPreferences.getString("putAddUserEvent", "");
        email = sharedPreferences.getString("email", "");

        serverUrl += "title=" + title.getText();
        serverUrl += "&date=" + date.getText();
        serverUrl += "&time=" + time.getText();
        serverUrl += "&email=" + email;


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