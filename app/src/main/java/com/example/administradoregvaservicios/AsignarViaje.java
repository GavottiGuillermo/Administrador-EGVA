package com.example.administradoregvaservicios;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class AsignarViaje extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
                                                                TimePickerDialog.OnTimeSetListener {
    public String txtNumAsociado, txtNombrePaciente, txtDomicilio, txtTelefono, txtObservaciones;
    String txtNombre;
    String txtTitulo,txtLugar,txtDescripcion,txtHospitalDestino;
    static String txtFecha,txtHora,txtChofer,txtEmailChofer;
    static TextView txtVFecha,txtVHora;
    Calendar cal;
    static int dia;
    static int mes;
    static int anio;
    static int hora;
    static int min;
    Button btnAsignar;
    Spinner spChoferes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_paciente);

        Button btnFecha = findViewById(R.id.idBtnFecha);
        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        Button btnHora = findViewById(R.id.idBtnHora);
        btnHora.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
        crearSpinnerChoferes();
        Button btnAsignar = findViewById(R.id.idBtnAsignar);
        btnAsignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatosViaje();
                if (!txtNumAsociado.isEmpty() && !txtNombrePaciente.isEmpty() && !txtDomicilio.isEmpty() &&
                        !txtTelefono.isEmpty() && !txtObservaciones.isEmpty() && !txtFecha.isEmpty() && !txtHora.isEmpty() &&
                        !txtHospitalDestino.isEmpty() && !txtChofer.isEmpty()) {
                    AsignarViaje(view);
                }else {
                    Toast.makeText(AsignarViaje.this, "Debe completar todos los campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setDatosViaje (){
        EditText edtNumAsociado = findViewById(R.id.idNumeroAsociado);
        EditText edtNombrePaciente = findViewById(R.id.idNombrePaciente);
        EditText edtDomicilioPaciente = findViewById(R.id.idDireccionPaciente);
        EditText edtTelefono = findViewById(R.id.idTelefonoPaciente);
        EditText edtObservaciones = findViewById(R.id.idObservaciones);
        EditText edtHospitalDestino = findViewById(R.id.idHospitalDestino);

        txtNombrePaciente = edtNombrePaciente.getText().toString();
        txtNumAsociado = edtNumAsociado.getText().toString();
        txtDomicilio = edtDomicilioPaciente.getText().toString();
        txtTelefono = edtTelefono.getText().toString();
        txtObservaciones = edtObservaciones.getText().toString();
        txtHospitalDestino = edtHospitalDestino.getText().toString();
        txtChofer = spChoferes.getSelectedItem().toString();
        switch (txtChofer){
            case "Luis" : txtEmailChofer = "choferluis001@gmail.com";
                break;
            case "Alberto:" : txtEmailChofer = "guille_gavo9@hotmail.com";
                break;
            case "Carlos" : txtEmailChofer = "guille_gavo9@hotmail.com";
                break;
        }

        txtTitulo = txtChofer + " de " + txtDomicilio + " a " + txtHospitalDestino;
        txtLugar = txtDomicilio;
        txtDescripcion = "Número Asociado: " + txtNumAsociado + "\n" +
                "Apellido y Nombre: " + txtNombre + "\n" +
                "Teléfono Paciente: " + txtTelefono + "\n" +
                "Hospital de Destino: " + txtHospitalDestino +
                "Observaciones: " + txtObservaciones + "\n" +
                "Chofer: " + txtChofer;
    }

    public static class DatePickerFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendario = Calendar.getInstance();
            anio = calendario.get(Calendar.YEAR);
            mes = calendario.get(Calendar.MONTH);
            dia = calendario.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), anio, mes, dia);
        }
    }

    public static class TimePickerFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendario = Calendar.getInstance();
            hora = calendario.get(Calendar.HOUR_OF_DAY);
            min = calendario.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hora,min,false);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        anio = year;
        mes = month;
        dia = dayOfMonth;

        txtFecha = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
        txtVFecha = findViewById(R.id.idTxtVFecha);
        txtVFecha.setText(txtFecha);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);

        hora = hourOfDay;
        min = minute;

        txtHora = hourOfDay + ":" + minute;
        txtVHora =findViewById(R.id.idTxtVHora);
        txtVHora.setText(txtHora);
    }
    @SuppressLint("NotConstructor")
    public void AsignarViaje(View view){
        Calendar cal_aux = Calendar.getInstance();
        cargarFechaTurno(cal_aux);

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        cargarIntent(intent, cal_aux);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(AsignarViaje.this, "Debe descargar Google Calendar para realizar la acción.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarIntent(Intent intent, Calendar cal_aux) {
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal_aux.getTimeInMillis());
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, txtTitulo);
        intent.putExtra(CalendarContract.Events.ALL_DAY, false);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, txtLugar);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, txtDescripcion);
        intent.putExtra(Intent.EXTRA_EMAIL, txtEmailChofer);
    }

    private void cargarFechaTurno(Calendar cal_aux) {
        cal_aux.set(Calendar.YEAR, anio);
        cal_aux.set(Calendar.MONTH, mes);
        cal_aux.set(Calendar.DAY_OF_MONTH, dia);
        cal_aux.set(Calendar.HOUR_OF_DAY, hora);
        cal_aux.set(Calendar.MINUTE, min);
    }

    public void crearSpinnerChoferes() {
        spChoferes = findViewById(R.id.idSpChoferes);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.choferes, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChoferes.setAdapter(adapter);
        //spChoferes.setOnItemSelectedListener(this);
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        txtChofer = parent.getItemAtPosition(position).toString();
//    }






}

