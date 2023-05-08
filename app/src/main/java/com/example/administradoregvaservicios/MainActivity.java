package com.example.administradoregvaservicios;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView txtVQuery;
    private int PermisoReadCalendar = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED)){
            solicitarPermisoLectura();
        }

        txtVQuery = findViewById(R.id.infoCalendario);
        Cursor cursor;
        cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI,null, null, null, null);

        if(cursor!=null && cursor.getCount()>0){
            StringBuilder strBuilderResulQuery = new StringBuilder();
            int idCuenta = cursor.getColumnIndex(CalendarContract.Events.ACCOUNT_NAME);
            int idTitulo = cursor.getColumnIndex(CalendarContract.Events.TITLE);
            int idFecha = cursor.getColumnIndex((CalendarContract.Events.DTSTART));

            while (cursor.moveToNext()){
               if(cursor.getString(idCuenta).equals("gavottiguillermo@gmail.com")){
                   System.out.println(cursor.getString(idCuenta));
                   strBuilderResulQuery.append(cursor.getString(idTitulo) + " / ")
                        /*+ cursor.getString(1) + " / "
                        + cursor.getString(2) + "\n")*/;
                }

            }
            txtVQuery.setText(strBuilderResulQuery.toString());
        }else{
            txtVQuery.setText("No hay viajes");
        }
    }

    private void solicitarPermisoLectura() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CALENDAR)) {

            new AlertDialog.Builder(this)
                    .setTitle("Se Necesita Permiso")
                    .setMessage("Permiso Necesario para Acceder al Calendario")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_CALENDAR}, PermisoReadCalendar);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_CALENDAR}, PermisoReadCalendar);
        }
    }




//        String[] projection = new String[] { "calendar_id", "title", "description",
//                "dtstart", "dtend", "eventLocation" };


    public void ejecutarAsignarViaje(View vista){
        Intent datosViaje = new Intent(this, AsignarViaje.class);
        startActivity(datosViaje);
    }

}


//        Cursor cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI, //(opción) ""Uri.parse("content://com.android.calendar/events")
//                colProyectadas,
//                null,
//                null,
//                null);
