package com.sainet.mislugaresapp;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.sainet.mislugaresapp.connections.ExternalPetitions;
import com.sainet.mislugaresapp.connections.InternalDataBase;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    CallbackManager callbackManager;
    InternalDataBase internalDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.activity_login);
            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            Button btnRegistroEmail = (Button) findViewById(R.id.btnRegitraEmail);
            TextView txtIniciaSesion = (TextView) findViewById(R.id.txtIniciarSesion);
            loginButton.setOnClickListener(this);
            loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
            callbackManager = CallbackManager.Factory.create();
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    try {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());
                                        try {
                                            String profileImageUrl = ImageRequest.getProfilePictureUri(object.optString("id"), 500, 500).toString();
                                            //Log.i("IMG", profileImageUrl);
                                            internalDataBase = new InternalDataBase(LoginActivity.this);
                                            internalDataBase.open();
                                            internalDataBase.delete("Perfil", "");
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("Email", object.getString("email"));
                                            contentValues.put("Nombre", object.getString("first_name"));
                                            contentValues.put("Apellido", object.getString("last_name"));
                                            contentValues.put("Celular", "");
                                            contentValues.put("Foto", profileImageUrl);
                                            contentValues.put("Sexo", object.getString("gender"));
                                            contentValues.put("Ciudad", "");
                                            internalDataBase.insert("Perfil", contentValues, "");
                                            internalDataBase.close();
                                            ExternalPetitions externalPetitions = new ExternalPetitions(LoginActivity.this);
                                            //String res = externalPetitions.crearUsuario("1", "");
                                            //Log.e("res", res);
                                        } catch (Exception e) {
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,email,first_name,last_name,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.d("HelloFacebook", String.format("Error: %s", exception.toString()));
                    String title = "Error";
                    String alertMessage = exception.getMessage();
                    showResult(title, alertMessage);
                }

                private void showResult(String title, String alertMessage) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(title)
                            .setMessage(alertMessage)
                            .setPositiveButton("Ok", null)
                            .show();
                }
            });

            txtIniciaSesion.setOnClickListener(this);
            btnRegistroEmail.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtIniciarSesion:
                Intent intent2 = new Intent(this, IniciarSesionActivity.class);
                startActivity(intent2);
                break;
            case R.id.btnRegitraEmail:
                Intent intent = new Intent(this, RegistroEmailActivity.class);
                startActivity(intent);
                break;
        }

    }
}
