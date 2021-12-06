package com.example.eventified;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthException;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class SignUp extends AppCompatActivity {


    EditText txtEmail;
    EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void onPressCreate(View view) {

        final RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);

        String serverUrl = ""; //Inputs SignUpLambda URL

        EditText txtEmail = findViewById(R.id.username);
        EditText txtPassword = findViewById(R.id.password);
        //TODO: Should hash passwords before they are sent, not done as not expected to be used outside of testing at this time

        serverUrl += "myUsername=" + txtEmail.getText() + "&myPassword=" + txtPassword.getText();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, serverUrl, null,

                response -> {
                    try {
                        boolean relpy = response.getBoolean("response");
                        requestQueue.stop();
                        if(relpy)
                        {
                            this.onLoginSuccess();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Username already taken", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Error: something with the request is wrong", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        requestQueue.stop();
                    }
                },
                error -> {
                    Toast.makeText(getApplicationContext(), "Error: something with Volley is wrong", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    requestQueue.stop();
                }) {
        };
        requestQueue.add(stringRequest);

    }

    private void onLoginSuccess() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("email", txtEmail.getText().toString());
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void onJoinError(AuthException e) {
        this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show());
    }

    public void onPressLogIn(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}