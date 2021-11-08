package com.example.eventified;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.core.Amplify;

public class LoginAmplify extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_amplify);
    }

    public void onPressSignIn(View view) {
        EditText txtEmail = findViewById(R.id.username);
        EditText txtPassword = findViewById(R.id.password);
        
        Amplify.Auth.signIn(
                txtEmail.getText().toString(),
                txtPassword.getText().toString(),
                this::onLoginSuccess,
                this::onLoginError
        );
    }

    private void onLoginError(AuthException e) {
        this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void onLoginSuccess(AuthSignInResult authSignInResult) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onPressCreate(View view) {
        Intent intent = new Intent(this, SignUpAmplify.class);
        startActivity(intent);
    }
}