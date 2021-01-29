package com.example.agenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.ui.tareas.TareasFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class VerTareaActivity extends AppCompatActivity {
    final Context context=this;
    TextView tvNombre;
    TextView tvFecha;
    TextView tvDesc;
    TextView tvHora;
    ImageView btnBack;

    FloatingActionButton btnEditWork, btneliminar;

    BaseDatos bd;

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_tarea);

        tvNombre = (TextView) findViewById(R.id.txt_actividad);
        tvFecha = (TextView) findViewById(R.id.txt_fecha);
        tvHora =  (TextView) findViewById(R.id.txt_hora);
        tvDesc = (TextView) findViewById(R.id.txt_des);
        btnBack = (ImageView) findViewById(R.id.btnBackWorksActivity);

        btnEditWork = (FloatingActionButton) findViewById(R.id.edit_work_btn);
        btneliminar=(FloatingActionButton)findViewById(R.id.elim_work_btn);
        id = getIntent().getExtras().getString("ID_TAREA");
        bd = new BaseDatos(this);
        try {
            ObtenDatosYMuestra();
        }catch (Exception e){
            tvDesc.setText(e.getMessage());
        }


        btnEditWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerTareaActivity.this, EditarTareaActivity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    AlertDialog.Builder del=new AlertDialog.Builder(context);
                    del.setMessage("Estas seguro de eliminar la tarea?");
                    del.setCancelable(false);
                    del.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            bd.DeleteWork(id);
                            Toast.makeText(getApplicationContext(),"Se elimino Correctamente",Toast.LENGTH_SHORT).show();
                            VerTareaActivity.this.finish();
                            Intent intent = new Intent(VerTareaActivity.this, MainActivity.class);
                            intent.putExtra("ID", id);
                            startActivity(intent);
                            finish();
                        }
                    });
                    del.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    });

                    del.show();

            }
        });
    }

    public void ObtenDatosYMuestra(){
        Cursor datos = bd.getWorkById(id);
        int id;
        String name, date, desc, h;
        while(datos.moveToNext()){
            name = datos.getString(datos.getColumnIndex("nombre"));
            date = datos.getString(datos.getColumnIndex("FM"));
            h = datos.getString(datos.getColumnIndex("HM"));
            desc = datos.getString(datos.getColumnIndex("descripcion"));
            tvNombre.setText(name);
            tvFecha.setText(date);
            tvHora.setText(h);
            tvDesc.setText(desc);
        }
    }


}
