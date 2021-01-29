package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EditarTareaActivity extends AppCompatActivity {

    EditText nombre;

    EditText FE;
    EditText HO;
    EditText descripcion;
    String id;
    BaseDatos db;
    Button btnActualizar, BTFecha, BTHora;

    int dia, mes, anio, hora, min,tem,con=0,idA;
    String fecha,hour,MN, HR,fechaM;
    String temMIN="",temHOR="",temParte,D="",M="",A="";
    Boolean bandera = false;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarea);

        db = new BaseDatos(this);

        nombre = (EditText) findViewById(R.id.TXT_Nombre_ACT);
        FE = (EditText) findViewById(R.id.TXT_FECHA);
        HO = (EditText) findViewById(R.id.TXT_HORA);
        descripcion = (EditText) findViewById(R.id.TXT_DCP_ACT);
        btnActualizar = (Button) findViewById(R.id.BTN_ACT);
        id = getIntent().getExtras().getString("ID");
        FE.setEnabled(false);
        HO.setEnabled(false);
        BTFecha =  (Button) findViewById(R.id.Date);
        BTHora =  (Button) findViewById(R.id.BtnHora);

        ObtenDatosYMuestra();

        BTFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsignarFecha(v);
            }
        });
        BTHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsignarHora(v);
            }
        });
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bandera){
                    fechaM = FE.getText().toString();
                    for(int i = 0; i < fechaM.length(); i++){

                        if(fechaM.charAt(i) == '-'){
                            con = con + 1;
                        }

                        if((fechaM.charAt(i) != '-') && (con == 0)){
                            D = D + fechaM.charAt(i);
                        }

                        if((fechaM.charAt(i) != '-') && (con == 1)){
                            M = M + fechaM.charAt(i);
                        }

                        if((fechaM.charAt(i) != '-') && (con == 2)){
                            A = A + fechaM.charAt(i);
                        }

                    }

                    dia =  Integer.parseInt(D);
                    mes = Integer.parseInt(M);
                    anio = Integer.parseInt(A);

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
                    fecha = FE.getText().toString();
                }

                idA = Integer.parseInt(id);
                db.UpdateWork(id, nombre.getText().toString(), fecha, FE.getText().toString(),hour,HO.getText().toString(), descripcion.getText().toString());
                db.UpdateActivo(idA,"1");
                finish();
                Toast.makeText(getApplicationContext(), "Actividad Actualizada Correctamente" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditarTareaActivity.this, MainActivity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
                finish();
            }
        });

    }




    public void ObtenDatosYMuestra(){
        Cursor datos = db.getWorkById(id);
        int id;
        String name, date, desc, h;
        while(datos.moveToNext()){
            name = datos.getString(datos.getColumnIndex("nombre"));
            date = datos.getString(datos.getColumnIndex("FM"));
            h = datos.getString(datos.getColumnIndex("HM"));
            desc = datos.getString(datos.getColumnIndex("descripcion"));
            nombre.setText(name);
            FE.setText(date);
            HO.setText(h);
            descripcion.setText(desc);
        }
    }

    public void AsignarFecha(View view){
        try{
            Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            anio = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.Calendar,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            FE.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(month+1) + "-" + String.valueOf(year) );

                        }
                    }, anio, mes, dia);
            datePickerDialog.show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void AsignarHora(View view){
        try{
            Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            min= c.get(Calendar.MINUTE);


            TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.Calendar, new TimePickerDialog.OnTimeSetListener() {
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

                    HO.setText(temHOR + ":" + temMIN);


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
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
