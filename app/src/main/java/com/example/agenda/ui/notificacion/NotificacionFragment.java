package com.example.agenda.ui.notificacion;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.agenda.BaseDatos;
import com.example.agenda.R;
import com.example.agenda.VerTareaActivity;
import com.example.agenda.Work;
import com.example.agenda.ui.nuevatarea.NuevaTareaFragment;
import com.example.agenda.ui.tareas.TareasFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NotificacionFragment extends Fragment  {

    private NotificacionViewModel notificacionViewModel;
    ListView listWorks;
    ArrayAdapter<String> adapter;
    ArrayList<String> nombre_tareas;
    BaseDatos dataBaseWorks;
    ArrayList<Work> tareasBD;
    Map<String, Integer> mapaTareas;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificacionViewModel =
                ViewModelProviders.of(this).get(NotificacionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notificacion, container, false);
       // final TextView textView = root.findViewById(R.id.text_slideshow);
        notificacionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        dataBaseWorks = new BaseDatos(getActivity());
        tareasBD = new ArrayList<Work>();
        tareasBD = obtenDatos();
        nombre_tareas = new ArrayList<String>();
        mapaTareas = new HashMap<String, Integer>();
        llenaAreglo();
        llenarMapa();

        listWorks = (ListView) root.findViewById(R.id.list_noti);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nombre_tareas);

        listWorks.setAdapter(adapter);

        listWorks.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String nombre = adapter.getItem(position);

                //Toast.makeText(getApplicationContext(), "ID: " + mapaTareas.get(nombre), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), VerTareaActivity.class);
                intent.putExtra("ID_TAREA", mapaTareas.get(nombre).toString());
                startActivity(intent);


            }
        });
    


        return root;
    }

    public ArrayList<Work> obtenDatos(){
        Cursor datos = dataBaseWorks.getTareas();
        ArrayList tareas = new ArrayList<Work>();
        int id;
        String name, date, time, desc, HM, FM,activo;

        while(datos.moveToNext()){
            activo = datos.getString(datos.getColumnIndex("activo"));

            if(activo.equals("0")){
                id = datos.getInt(datos.getColumnIndex("id_tarea"));
                name = datos.getString(datos.getColumnIndex("nombre"));
                date = datos.getString(datos.getColumnIndex("fecha"));
                FM = datos.getString(datos.getColumnIndex("FM"));
                time = datos.getString(datos.getColumnIndex("hora"));
                HM = datos.getString(datos.getColumnIndex("HM"));

                desc = datos.getString(datos.getColumnIndex("descripcion"));
                Work work = new Work(id, name, date,FM,desc,time,HM,activo);
                tareas.add(work);
            }

        }
        return  tareas;
    }

    public void  llenaAreglo(){
        for (int i = 0; i < tareasBD.size() ; i++ ){
            nombre_tareas.add(tareasBD.get(i).getNombre());
        }
    }

    public  void  llenarMapa(){
        String llave;
        int valor;
        for (int i=0; i<tareasBD.size(); i++){
            llave = tareasBD.get(i).getNombre();
            valor = tareasBD.get(i).getId_work();
            mapaTareas.put(llave, valor);
        }
    }
    /*@Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(
                true // default to enabled
        ) {
            @Override
            public void handleOnBackPressed() {



            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(
                this, // LifecycleOwner
                callback);
    }*/




}//XD