package com.example.agenda.ui.configuracion;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.agenda.BaseDatos;
import com.example.agenda.R;
import com.example.agenda.ServiceNoti;

import java.util.Objects;

public class ConfiguracionFragment extends Fragment {

    private ConfiguracionViewModel configuracionViewModel;
    Boolean PASO;
    TextView txtS;
    TextView txtmen;
    Button btnS;
    BaseDatos bd;
    Switch STBBJ;
    Boolean ST;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        configuracionViewModel =
                ViewModelProviders.of(this).get(ConfiguracionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_configuracion, container, false);
        //final TextView textView = root.findViewById(R.id.text_share);
        configuracionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);
            }
        });
        txtS = (TextView) root.findViewById(R.id.txtServer);
        btnS = (Button) root.findViewById(R.id.btnServer);
        STBBJ =(Switch) root.findViewById(R.id.STH_BBJ);
        PASO = isMyServiceRunning(ServiceNoti.class);
        txtmen = (TextView) root.findViewById(R.id.textView8);
        btnS.setEnabled(false);

        bd = new BaseDatos(getActivity());

        try{
            Cursor datos = bd.getConfig("1");
            String STH = "";
            while(datos.moveToNext()){
                STH = datos.getString(1);
            }

            if("off".equals(STH)){
                STBBJ.setChecked(false);

            }else if(STH.equals("on")){
                STBBJ.setChecked(true);
            }
            //txtmen.setText(STH);
            STBBJ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getActivity())) {

                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getActivity().getPackageName()));
                        startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);

                    }
                    if (isChecked) {
                        // The toggle is enabled
                        bd.UpdateConfig("1","on");
                        Toast.makeText(getActivity(), "ON", Toast.LENGTH_SHORT).show();
                    } else {
                        // The toggle is disabled
                        bd.UpdateConfig("1","off");
                        Toast.makeText(getActivity(), "OFF", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e){
            txtmen.setText(e.getMessage());
        }

        if(PASO){
            //Desactivo el servicio
            txtS.setText("Activar Notificasiones");
            btnS.setText("Activar");

        }else{
            //Activo el servicio
            txtS.setText("Desactivar Notificasiones");
            btnS.setText("Desactivar");

        }

        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PASO){
                    //Desactivo el servicio
                    txtS.setText("Desactivar Notificasiones");
                    btnS.setText("Desactivar");
                    getActivity().startService(new Intent(getActivity(), ServiceNoti.class));
                    Toast.makeText(getActivity(), "Servicio creado", Toast.LENGTH_SHORT).show();
                }else{
                    //Activo el servicio
                    txtS.setText("Activar Notificasiones");
                    btnS.setText("Activar");
                    getActivity().stopService(new Intent(getActivity(), ServiceNoti.class));
                }

                PASO = isMyServiceRunning(ServiceNoti.class);
            }
        });

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
}