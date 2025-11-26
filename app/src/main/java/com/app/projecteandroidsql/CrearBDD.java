package com.app.projecteandroidsql;

import android.os.Bundle;
import android.util.Log;
import androidx.activity.ComponentActivity;

import com.app.projecteandroidsql.data.DB;
import com.app.projecteandroidsql.utils.Constants;

public class CrearBDD extends ComponentActivity {

    private DB db;
    public static final String TAG = "CrearBDD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the database
        db = new DB(this);
        Log.d(TAG, "Base de dades inicialitzada");

        // Load and display movies in Logcat
        carregarTaules();
    }

    /**
     * Function to load movies from the database and display them in Logcat
     */
    private void carregarTaules() {
        Log.d(TAG, "Començant la càrrega de pel·lícules de la base de dades");

        // Query the database
        try (var cursor = db.getReadableDatabase().query(
                Constants.TABLE_NAME_AUTOR,
                null,
                null,
                null,
                null,
                null,
                null
        )) {
            Log.d(TAG, "Consulta SQL executada. Nombre de resultats: " + cursor.getCount());

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_AUTOR_ID));
                String nom = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_NOM));
                String cognom = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_COGNOM));
                String nom_artistic = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_NOM_ARTISTIC));
                int data = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_DATA_NAIXEMENT));

                Log.i(TAG, String.format(
                        "Pel·lícula trobada: ID=%d, Títol=%s, Any=%d, Director=%s, Valoració=%.1f",
                        id, nom, cognom, nom_artistic, data
                ));
            }
        }

        Log.d(TAG, "Càrrega de pel·lícules finalitzada");
    }
}
