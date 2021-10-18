package com.example.eventified;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ClubSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_search);
        configureHomeButton();
    }

    public void configureHomeButton()
    {
        ImageButton homeButton = (ImageButton) findViewById(R.id.HomeButton);
        homeButton.setOnClickListener(v -> finish());
    }
}