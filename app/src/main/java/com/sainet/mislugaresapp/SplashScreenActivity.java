package com.sainet.mislugaresapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.sainet.mislugaresapp.connections.ExternalPetitions;
import com.sainet.mislugaresapp.connections.InternalDataBase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Desarrollador5 on 12/07/2016.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 500;
    private static final int REQUEST = 0;
    // Set the duration of the splash screen
    public static RelativeLayout rlyCargando;
    TimerTask task;
    boolean continuar = true;

    int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.splash_screen);
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        InternalDataBase internalDataBase = new InternalDataBase(SplashScreenActivity.this);
                        internalDataBase.open();
                        Cursor c = internalDataBase.read("select count(*),Email from Perfil");
                        if (c.moveToFirst() && c.getInt(0) > 0) {
                            Intent intent2 = new Intent(SplashScreenActivity.this, MainActivity.class);
                            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(intent2);


                        } else {
                            Intent mainIntent = new Intent().setClass(SplashScreenActivity.this, LoginActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(mainIntent);
                        }
                        internalDataBase.close();
                        //finish();
                    } catch (Exception e) {
                    }
                }
            };
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permisos();
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                rlyCargando = (RelativeLayout) findViewById(R.id.rltCargando);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setIndeterminate(true);
                Timer timer = new Timer();
                timer.schedule(task, SPLASH_SCREEN_DELAY);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciarCambioActividad() {
        if (continuar && cont >= 2) {
            rlyCargando = (RelativeLayout) findViewById(R.id.rltCargando);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setIndeterminate(true);
            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);
        } else {
            Toast.makeText(this, R.string.msjErrorPermisos, Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void permisos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Log.i("Permisos", "requestPermissions");
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION}, REQUEST);
        } else {
            //Log.i("Permisos", "requestPermissions permitidos");
            cont = 4;
            iniciarCambioActividad();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    continuar = false;
                } else {
                    cont++;
                }
            }
            iniciarCambioActividad();
        }
    }


}