package com.example.eventified;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureClubButton();
    }

    public void mainActivityClick(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void configureClubButton()
    {
        ImageButton searchButton = (ImageButton) findViewById(R.id.SearchButton);
        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClubSearch.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}