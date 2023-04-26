package com.example.administradoregvaservicios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }

    public void ejecutarAsignarViaje(View vista){
        Intent datosViaje = new Intent(this, AsignarViaje.class);
        startActivity(datosViaje);
    }


}