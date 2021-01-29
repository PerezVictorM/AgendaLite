package com.example.agenda.ui.nuevatarea;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.example.agenda.BaseDatos;
import com.example.agenda.R;
import com.example.agenda.ServiceNoti;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class NuevaTareaFragment extends Fragment implements View.OnClickListener {
    private NuevaTareaViewModel nuevatareasViewModel;

    EditText etTarea, etDia, etMes, etAnio, etHora, etMin, etDesc;
    Button btnGuardar, date,btnHora;
    TextView error;
    //Work objwork;
    BaseDatos dataBaseWorks;
    int dia, mes, anio, hora, min,tem;
    String fecha, hour,temMIN="",MN, HR,temHOR="",fechaM;
    Boolean bandera = false;

    private Context con=null;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        con = this.getActivity();
        nuevatareasViewModel =
                ViewModelProviders.of(this).get(NuevaTareaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_nueva_tarea, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        nuevatareasViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // textView.setText(s);
            }
        });
        dataBaseWorks = new BaseDatos(getActivity());

        error = (TextView) root.findViewById(R.id.txtTitulo);

        etTarea = (EditText) root.findViewById(R.id.TXT_Nombre);
        etDia = (EditText) root.findViewById(R.id.TXT_DIA);
        etMes = (EditText) root.findViewById(R.id.TXT_MES);
        etAnio = (EditText) root.findViewById(R.id.TXT_AÑO);
        etDesc = (EditText) root.findViewById(R.id.TXT_DCP);
        etHora = (EditText) root.findViewById(R.id.TXT_HORA);
        etMin = (EditText) root.findViewById(R.id.TXT_MINUTOS);
        //EDIT TEXT DESHABILITADOS
        etDia.setEnabled(false);
        etMes.setEnabled(false);
        etAnio.setEnabled(false);
        etHora.setEnabled(false);
        etMin.setEnabled(false);
        btnGuardar = (Button) root.findViewById(R.id.BTN_AG);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
            }
        });
        date = (Button) root.findViewById(R.id.Date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsignarFecha(v);
            }
        });
        btnHora = (Button)root.findViewById(R.id.BtnHora);
        btnHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsignarHora(v);
            }
        });


        return root;
    }





    public void AsignarFecha(View view){
            try{
                Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                anio = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(con,R.style.Calendar,
                        new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    etDia.setText(String.valueOf(dayOfMonth));
                    etMes.setText(String.valueOf(month+1));
                    etAnio.setText(String.valueOf(year));
                    }
                 }, anio, mes, dia);
                datePickerDialog.show();
            }catch (Exception e){
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
    }

    public void AsignarHora(View view){
        try{
            Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            min= c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(con,R.style.Calendar, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    if(minute<10){
                        temMIN = "0" + String.valueOf(minute);
                    }else{
                        temMIN = String.valueOf(minute);
                    }
                    if(hourOfDay<10){
                        temHOR= "0" + String.valueOf(hourOfDay);
                    }else{
                        temHOR = String.valueOf(hourOfDay);
                    }
                    etHora.setText(String.valueOf(temHOR));
                    etMin.setText(String.valueOf(temMIN));

                    if((minute - 5)<0){
                        tem = minute - 5;
                        tem = tem * -1;
                        tem = 60 - tem;

                        MN = String.valueOf(tem);

                        if((hourOfDay - 1) < 0){
                            HR = "23";
                            bandera = true;
                        }else{
                            tem = hourOfDay - 1;

                            if(tem<10){
                                temHOR= "0" + String.valueOf(tem);
                            }else{
                                temHOR = String.valueOf(tem);
                            }
                            HR= temHOR;
                        }

                    }else{
                        HR = String.valueOf(hourOfDay);
                        tem = minute - 5;

                        if(tem<10){
                            temMIN = "0" + String.valueOf(tem);
                        }else{
                            temMIN = String.valueOf(tem);
                        }

                        MN = temMIN;
                    }
                    hour = HR + ":" + MN;



                }
            },hora,min,false);
            timePickerDialog.show();

        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void guardarDatos(){
        try {
            if (etTarea.getText().toString().equals("") ||  etDia.getText().toString().equals("") || etMes.getText().toString().equals("") || etAnio.getText().toString().equals("") || etHora.getText().toString().equals("") || etMin.getText().toString().equals("") || etDesc.getText().toString().equals("")){
                Toast.makeText(getActivity(), "No puede dejar espacios vacios", Toast.LENGTH_SHORT).show();
            }else {
                String work;
                String hora;
                String ACT = "1";
                String description;
                //NUEVAS VARIABLES
                work = etTarea.getText().toString();

                if (bandera){
                    dia =  Integer.parseInt(etDia.getText().toString());
                    mes = Integer.parseInt(etMes.getText().toString());
                    anio = Integer.parseInt(etAnio.getText().toString());

                    if ((dia-1)<1){
                        if((mes - 1)<1){
                            dia = 31;
                            mes = 12;
                            anio = anio - 1;
                        }else{
                            mes = mes - 1;
                            switch (mes){
                                case 1:
                                    dia = 31;
                                    break;

                                case 2:
                                    dia = 28;
                                    break;

                                case 3:
                                    dia = 31;
                                    break;

                                case 4:
                                    dia=30;
                                    break;

                                case 5:
                                    dia=31;
                                    break;

                                case 6:
                                    dia=30;
                                    break;

                                case 7:
                                    dia=31;
                                    break;

                                case 8:
                                    dia=31;
                                    break;

                                case 9:
                                    dia=30;
                                    break;

                                case 10:
                                    dia=31;
                                    break;

                                case 11:
                                    dia=30;
                                    break;
                            }
                        }
                    }else{
                        dia = dia - 1;
                    }

                    fecha = dia + "-" + mes + "-" +anio;

                }else{
                    fecha = etDia.getText().toString() + "-" + etMes.getText().toString() + "-" + etAnio.getText().toString();
                }

                fechaM = etDia.getText().toString() + "-" + etMes.getText().toString() + "-" + etAnio.getText().toString();
                hora = etHora.getText().toString() + ":" + etMin.getText().toString();
                description = etDesc.getText().toString();
                dataBaseWorks.guardaDatos(work, fecha,fechaM, hour,  hora, ACT, description);
                etTarea.setText("");
                etDia.setText("");
                etMes.setText("");
                etAnio.setText("");
                etHora.setText("");
                etMin.setText("");
                etDesc.setText("");
                Toast.makeText(getActivity(), "La Actividad se ha agregado correctamente", Toast.LENGTH_SHORT).show();


            }
        }catch (Exception e){
            etDesc.setText(e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {

    }
}