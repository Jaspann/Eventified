package com.example.eventified;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.auth.result.AuthSignUpResult;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.datastore.DataStoreException;
import com.amplifyframework.datastore.DataStoreItemChange;
import com.amplifyframework.datastore.generated.model.User;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class SignUpAmplify extends AppCompatActivity {


    EditText txtEmail;
    EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_amplify);
    }

    public void onPressCreate(View view) {

        final RequestQueue requestQueue = Volley.newRequestQueue(SignUpAmplify.this);

        String serverUrl = ""; //Input SignUpAmplify URL Here

        EditText txtEmail = findViewById(R.id.username);
        EditText txtPassword = findViewById(R.id.password);

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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void onJoinError(AuthException e) {
        this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show());
    }

    public void onPressLogIn(View view) {
        Intent intent = new Intent(this, LoginAmplify.class);
        startActivity(intent);
    }
}