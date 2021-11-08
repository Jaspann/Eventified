package com.example.eventified;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.auth.result.AuthSignUpResult;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.datastore.DataStoreException;
import com.amplifyframework.datastore.DataStoreItemChange;
import com.amplifyframework.datastore.generated.model.User;

public class EmailConfirmationAmplify extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation_amplify);
    }

    public void onPressContinue(View view) {
        EditText txtCode = findViewById(R.id.confirmation_code);
        
        Amplify.Auth.confirmSignUp(
                getEmail(),
                txtCode.getText().toString(),
                this::onSuccess,
                this::onError
        );
    }

    private void onError(AuthException e) {
        this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void onSuccess(AuthSignUpResult authSignUpResult) {
        reLogin();
    }

    private void reLogin() {
        String username = getEmail();
        String password = getPassword();
        Amplify.Auth.signIn(
                username,
                password,
                this::onLoginSuccess,
                this::onError
        );
    }

    private void onLoginSuccess(AuthSignInResult authSignInResult) {
        String userId = Amplify.Auth.getCurrentUser().getUserId();
        String email = getEmail();

        Amplify.DataStore.save(
                User.builder().id(userId).name(email).build(),
                this::onSavedSuccess,
                this::onError
        );
    }

    private <T extends Model> void onSavedSuccess(DataStoreItemChange<T> tDataStoreItemChange) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void onError(DataStoreException e) {
        this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private String getEmail()
    {
        return getIntent().getStringExtra("email");
    }

    private String getPassword()
    {
        return getIntent().getStringExtra("password");
    }
}