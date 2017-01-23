package com.sainet.mislugaresapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sainet.mislugaresapp.R;

/**
 * Created by t on 15/06/2016.
 */
public class UtilMethods {

    private static Dialog dialog = null;
    private Activity activity;
    public UtilMethods(Activity activity) {
        this.activity = activity;
    }
    public static void mostrarDialogMensaje(String mensaje, Activity activity) throws Exception {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);
        builder.setView(R.layout.dialog_mensajes);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        TextView txtMensaje = (TextView) dialog.findViewById(R.id.txtMensaje);
        ImageView btnClose = (ImageView) dialog.findViewById(R.id.btnClose);
        Button btnCerrar = (Button) dialog.findViewById(R.id.btnCerrar);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (!mensaje.equals(""))
            txtMensaje.setText(mensaje);


    }

}


