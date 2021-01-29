package com.example.agenda.ui.tareas;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.agenda.BaseDatos;
import com.example.agenda.R;
import com.example.agenda.ServiceNoti;
import com.example.agenda.VerTareaActivity;
import com.example.agenda.Work;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TareasFragment extends Fragment {

    private TareasViewModel tareasViewModel;
    ListView listWorks;
    ArrayAdapter<String> adapter;
    ArrayList<String> nombre_tareas;
    ImageView back;
    FloatingActionButton btnFloat;
    BaseDatos dataBaseWorks;
    ArrayList<Work> tareasBD;
    Map<String, Integer> mapaTareas;
    TextView ERROR, SinAct;
    Boolean PASO;
    private SQLiteDatabase BD;
    Cursor raw;
    BaseDatos bd;
    int i;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tareasViewModel =
                ViewModelProviders.of(this).get(TareasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tareas, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        tareasViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);
            }
        });



        dataBaseWorks = new BaseDatos(getActivity());
        tareasBD = new ArrayList<Work>();
        tareasBD = obtenDatos();
        nombre_tareas = new ArrayList<String>();
        mapaTareas = new HashMap<String, Integer>();
        llenaAreglo();
        llenarMapa();

        bd = new BaseDatos(getActivity());
        BD = bd.getWritableDatabase();

        raw = BD.rawQuery("SELECT * FROM config", null);

        while (raw.moveToNext()) {
            i += 1;
        }
        if(i == 0){
            bd.guardaConfig("1","off","on");
        }



        listWorks = (ListView) root.findViewById(R.id.list_work1);
        SinAct=(TextView) root.findViewById(R.id.txt_SinAct);
        listWorks.setEmptyView(SinAct);
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
        PASO = isMyServiceRunning(ServiceNoti.class);
        if(PASO){
            getActivity().startService(new Intent(getActivity(), ServiceNoti.class));
        }

        return root;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName()))  {
                return false;
            }
        }
        return true;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(
                true // default to enabled
        ) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(
                this, // LifecycleOwner
                callback);
    }



    public ArrayList<Work> obtenDatos(){
        Cursor datos = dataBaseWorks.getTareas();
        ArrayList tareas = new ArrayList<Work>();
        int id;
        String name, date, time, desc, HM, FM,activo;

        while(datos.moveToNext()){
            id = datos.getInt(datos.getColumnIndex("id_tarea"));
            name = datos.getString(datos.getColumnIndex("nombre"));
            date = datos.getString(datos.getColumnIndex("fecha"));
            FM = datos.getString(datos.getColumnIndex("FM"));
            time = datos.getString(datos.getColumnIndex("hora"));
            HM = datos.getString(datos.getColumnIndex("HM"));
            activo = datos.getString(datos.getColumnIndex("activo"));
            desc = datos.getString(datos.getColumnIndex("descripcion"));
            Work work = new Work(id, name, date,FM,desc,time,HM,activo);
            tareas.add(work);
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



}