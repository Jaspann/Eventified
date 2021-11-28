package com.example.eventified;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class ClubActivity extends AppCompatActivity {

    ImageView logo, banner;
    TextView name, description;
    Toolbar toolbar;

    String tempName, tempDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        logo = findViewById(R.id.club_logo);
        banner = findViewById(R.id.bannerImageView);
        name = findViewById(R.id.club_name);
        description = findViewById(R.id.club_description);
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getData();
        setData();

    }

    private void getData(){

        if(getIntent().hasExtra("name") && getIntent().hasExtra("description")){

            tempName = getIntent().getStringExtra("name");
            tempDesc = getIntent().getStringExtra("description");

        }else{
            Toast.makeText(this,"No Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        name.setText(tempName);
        toolbar.setTitle(tempName);
        description.setText(tempDesc);


        String logoUrl = "https://eventifiedbucketone.s3.amazonaws.com/logos/"+
                tempName.replace(' ', '+')+".png";

        Picasso.get().load(logoUrl).into(logo);

        String bannerUrl = "https://eventifiedbucketone.s3.amazonaws.com/banners/"+
                tempName.replace(' ', '+')+".jpg";

        Picasso.get().load(bannerUrl).into(banner);



    }
}