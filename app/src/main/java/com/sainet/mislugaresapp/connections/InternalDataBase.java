package com.sainet.mislugaresapp.connections;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.ArrayList;


/**
 * Created by Desarrolador5 on 07/07/2016.
 */
public class InternalDataBase {
    private static final String N_BD = "dbFarmaMujer";
    private static final int VERSION_BD = 50;
    private BDhelper nHelper;
    private Context nContexto;
    private SQLiteDatabase nBD;


    public InternalDataBase(Context nContexto) {
        this.nContexto = nContexto;
    }

    public InternalDataBase open() throws Exception {
        nHelper = new BDhelper(nContexto);
        nBD = nHelper.getWritableDatabase();
        return this;
    }

    public void close() throws Exception {
        nHelper.close();
    }

    public long insert(String tabla, ContentValues cv, String where) throws Exception {
        int id = (int) nBD.insertWithOnConflict(tabla, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            id = 10000 + update(tabla, cv, where);
        }
        return id;
    }


    public void commitPoolUpdate(ArrayList listSQL) throws Exception {
        if (listSQL != null && listSQL.size() > 0) {
            if (nBD != null) {
                nBD.beginTransaction();
                for (int i = 0; i < listSQL.size(); i++) {
                    //Log.e("i", "" + i);
                    nBD.execSQL(listSQL.get(i).toString());
                }
                nBD.setTransactionSuccessful();
                nBD.endTransaction();
            }
            listSQL.clear();
        }

    }

    public void execSQL(String sql) throws Exception {
        nBD.execSQL(sql);
    }

    public Cursor read(String sql) throws Exception {
        return nBD.rawQuery(sql, null);
    }

    public int update(String tabla, ContentValues cv, String where) throws Exception {
        return nBD.update(tabla, cv, where, null);
    }

    public int delete(String tabla, String where) throws Exception {
        return nBD.delete(tabla, where, null);
    }

    private class BDhelper extends SQLiteOpenHelper {
        BDhelper(Context context) {
            super(context, N_BD, null, VERSION_BD);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            /// Se crea un vector de la estructura para una rapida ejecucion y para evitar
            // que se muestre el sql cuando se compile con proguard
            String sql[] = new String[]{
                    "CREATE TABLE IF NOT EXISTS Perfil " +
                            "(_id INTENGER PRIMARY KEY ," +
                            "Email VARCHAR(100)," +
                            "Nombre VARCHAR(100)," +
                            "Apellido VARCHAR(100)," +
                            "Celular VARCHAR(100)," +
                            "DiaCumplanios VARCHAR(50)," +
                            "MesCumplanios VARCHAR(50)," +
                            "Sexo VARCHAR(50)," +
                            "Ciudad VARCHAR(150)," +
                            "Foto VARCHAR(10));"
            };

            for (String dato : sql) {
                db.execSQL(dato);
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String delete[] = new String[]{
                    "drop table IF EXISTS Perfil"};

            for (String dato : delete) {
                db.execSQL(dato);
            }
            FacebookSdk.sdkInitialize(nContexto);
            if (LoginManager.getInstance() != null)
                LoginManager.getInstance().logOut();
            onCreate(db);
        }
    }

}
