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

public class SignUpAmplify extends AppCompatActivity {


    EditText txtEmail;
    EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_amplify);
    }

    public void onPressCreate(View view) {
        txtEmail = findViewById(R.id.username);
        txtPassword = findViewById(R.id.password);

        Amplify.Auth.signUp(
                txtEmail.getText().toString(),
                txtPassword.getText().toString(),
                AuthSignUpOptions.builder().userAttribute(
                        AuthUserAttributeKey.email(), txtEmail.getText().toString()
                ).build(),
                this::onJoinSuccess,
                this::onJoinError
        );

    }

    private void onJoinSuccess(AuthSignUpResult authSignUpResult) {
        Intent intent = new Intent(this, EmailConfirmationAmplify.class);
        intent.putExtra("email", txtEmail.getText().toString());
        intent.putExtra("password", txtPassword.getText().toString());
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