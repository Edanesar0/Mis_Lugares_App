package com.sainet.mislugaresapp;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sainet.mislugaresapp.connections.ExternalPetitions;
import com.sainet.mislugaresapp.connections.InternalDataBase;

import org.json.JSONObject;

public class RegistroEmailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtTitulo;
    EditText edtEmail;
    EditText edtConfEmail;
    EditText edtPass;
    EditText edtConPass;

    TextInputLayout input_correo;
    TextInputLayout input_confirmacion_correo;
    TextInputLayout input_pass;
    TextInputLayout input_confirmacion_pass;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtTitulo = (TextView) findViewById(R.id.txtInicia);
        edtEmail = (EditText) findViewById(R.id.edt_correo);
        edtConfEmail = (EditText) findViewById(R.id.edt_conf_email);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        edtConPass = (EditText) findViewById(R.id.edt_conf_pass);

        input_correo = (TextInputLayout) findViewById(R.id.input_correo);
        input_confirmacion_correo = (TextInputLayout) findViewById(R.id.input_confirmacion_correo);
        input_pass = (TextInputLayout) findViewById(R.id.input_pass);
        input_confirmacion_pass = (TextInputLayout) findViewById(R.id.input_confirmacion_pass);
        btnGuardar = (Button) findViewById(R.id.btnRegitrar);
        btnGuardar.setOnClickListener(this);
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                input_correo.setError(null);

            }
        });
        edtConfEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                input_confirmacion_correo.setError(null);

            }
        });
        edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                input_pass.setError(null);

            }
        });
        edtConPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                input_confirmacion_pass.setError(null);

            }
        });
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btnRegitrar:
                    boolean cancel = false;
                    View focusView = null;
                    if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                        input_correo.setError(getString(R.string.error_vacio));
                        input_correo.requestFocus();
                        focusView = edtEmail;
                        cancel = true;
                    } else if (!isEmailValid(edtEmail.getText().toString())) {
                        input_correo.setError(getString(R.string.error_invalid_email));
                        focusView = edtEmail;
                        cancel = true;
                    }

                    if (!TextUtils.equals(edtEmail.getText().toString(), edtConfEmail.getText().toString())) {
                        input_confirmacion_correo.setError(getString(R.string.error_email));
                        input_confirmacion_correo.requestFocus();
                        focusView = edtConfEmail;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(edtPass.getText().toString())) {
                        input_pass.setError(getString(R.string.error_vacio));
                        input_pass.requestFocus();
                        focusView = edtPass;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(edtConPass.getText().toString())) {
                        input_confirmacion_pass.setError(getString(R.string.error_vacio));
                        input_confirmacion_pass.requestFocus();
                        focusView = edtConPass;
                        cancel = true;
                    }
                    if (!TextUtils.equals(edtPass.getText().toString(), edtConPass.getText().toString())) {
                        input_confirmacion_pass.setError(getString(R.string.error_pass));
                        input_confirmacion_pass.requestFocus();
                        focusView = edtConPass;
                        cancel = true;
                    }
                    if (cancel) {
                        focusView.requestFocus();
                    } else {
                        ExternalPetitions externalPetitions = new ExternalPetitions(this);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", edtEmail.getText().toString());
                        jsonObject.put("pass", edtPass.getText().toString());
                        jsonObject.put("mail", edtEmail.getText().toString());
                        jsonObject.put("init", edtEmail.getText().toString());
                        jsonObject.put("roles", "authenticated user");
                        jsonObject.put("status", "1");
                        externalPetitions.registrarUsuario(jsonObject.toString());
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("Email", edtEmail.getText().toString());
                        InternalDataBase internalDataBase = new InternalDataBase(this);
                        internalDataBase.open();
                        internalDataBase.insert("Perfil", contentValues, "");
                        internalDataBase.close();

                    }

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }
}
