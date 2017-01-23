package com.sainet.mislugaresapp.connections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.sainet.mislugaresapp.R;
import com.sainet.mislugaresapp.util.UtilMethods;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Desarrollor5 on 13/07/16.
 */
public class ExternalPetitions {
    public static boolean busqueda;
    private static String servidor = "http://108.179.199.76/~pruebadesarrollo";
    private static String url1 = "/restws/session/token";
    private static String url2 = "/user";


    private Activity activity;
    private InternalDataBase internalDataBase;

    private ProgressDialog pDialog;
    private String resultado;

    public ExternalPetitions(Activity activity) {
        this.activity = activity;
        internalDataBase = new InternalDataBase(activity);
    }


    public String registrarUsuario(String urlParameters) throws Exception {
        new MiTask().execute("1", servidor + url2, urlParameters);
        return resultado;
    }


    private String StreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }


    private class MiTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            try {
                if (activity != null) {
                    pDialog = new ProgressDialog(activity);
                    pDialog.setMessage(activity.getResources().getString(R.string.cargando));
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                }
            } catch (Exception e) {
                if (pDialog != null) {
                    pDialog.dismiss();
                }

            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url;
                HttpURLConnection client = null;
                DataOutputStream wr;
                InputStream is;
                switch (params[0]) {
                    case "1":
                        url = new URL(params[1]);
                        client = (HttpURLConnection) url.openConnection();
                        client.setRequestMethod("POST");
                        client.setUseCaches(false);
                        client.setDoInput(true);
                        client.setDoOutput(true);
                        client.setRequestProperty("Accept", "Application/json");
                        client.setRequestProperty("Content-Type", "application/json");
                        client.setRequestProperty("Authorization", " Basic cmVzdF9hcGk6MTIzNDU2Nzg5");
                        wr = new DataOutputStream(client.getOutputStream());
                        wr.writeBytes(params[2]);
                        wr.flush();
                        wr.close();
                        int status = client.getResponseCode();
                        if (status == HttpURLConnection.HTTP_OK || status == HttpURLConnection.HTTP_CREATED) {
                            is = client.getInputStream();
                            resultado = StreamToString(is);
                        } else {
                            is = client.getErrorStream();
                            resultado = StreamToString(is);
                        }
                        client.disconnect();
                        return resultado;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (pDialog != null)
                    pDialog.dismiss();
                InternalDataBase internalDataBase = new InternalDataBase(activity);
                internalDataBase.open();
                if (result != null && !result.equals("")) {
                    if (result.contains("406"))
                        UtilMethods.mostrarDialogMensaje(result, activity);
                    Object json = new JSONTokener(result).nextValue();
                    if (json instanceof JSONArray)
                        new JSONArray(result).get(0).toString();
                    else if (json instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("uri")) {
                            ContentValues values = new ContentValues();
                            values.put("_id",jsonObject.getString("id"));
                            internalDataBase.update("Perfil",values,"");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (pDialog != null)
                    pDialog.dismiss();
            }
        }
    }


    private class MiTaskBMP extends AsyncTask<String, Bitmap, Bitmap> {
        Bitmap bmp;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                InternalDataBase internalDataBase = new InternalDataBase(activity);
                internalDataBase.open();
                Picasso p = Picasso.with(activity);
                RequestCreator rq = p.load(params[0]);
                Bitmap bmp = rq.get();
                ContentValues values = new ContentValues();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
                return bmp;

            } catch (Exception e) {
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            try {
                if (pDialog != null)
                    pDialog.dismiss();
            } catch (Exception e) {
                if (pDialog != null)
                    pDialog.dismiss();
            } finally {
                if (pDialog != null)
                    pDialog.dismiss();
            }

        }

    }
}


