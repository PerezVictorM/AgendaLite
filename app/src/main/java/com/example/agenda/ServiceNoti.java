package com.example.agenda;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ServiceNoti extends Service {

    MyTask myTask;

    public ServiceNoti() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(this, "Servicio creado", Toast.LENGTH_SHORT).show();
        myTask = new MyTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myTask.execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Servicio destruído", Toast.LENGTH_SHORT).show();
        myTask.cancel(true);
    }

    private class MyTask extends AsyncTask<String, String, String> {

        public static final int REQUEST_CODE = 12345;
        private BaseDatos admin;
        private Cursor fila;
        private Cursor config;
        private SQLiteDatabase bd;
        private String HN,titulo;
        private final  static String CHANNEL_ID = "NOTIFICACION";
        private int alarma = 0;
        private DateFormat dateFormat;
        private String date;
        private boolean cent;
        Calendar calendario;
        int con = 1 ;
        int hora, min,dia,mes,ano;
        String fecha_sistema,hora_sistema,AC,ST="";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dateFormat = new SimpleDateFormat("HH:mm:ss");
            cent = true;
        }

        @Override
        protected String doInBackground(String... params) {
            while (cent){
                date = dateFormat.format(new Date());
                try {
                    publishProgress(date);
                    // Stop 5s
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            calendario = Calendar.getInstance();
            dia = calendario.get(Calendar.DAY_OF_MONTH);
            mes = calendario.get(Calendar.MONTH)+1;
            ano = calendario.get(Calendar.YEAR);
            hora = calendario.get(Calendar.HOUR_OF_DAY);
            min = calendario.get(Calendar.MINUTE);
            fecha_sistema = dia+"-"+mes+"-"+ano;
            hora_sistema = hora+":"+min;
            AC="1";


            admin = new BaseDatos(getApplicationContext());
            bd = admin.getWritableDatabase();
            if(bd!=null) {
                fila = bd.rawQuery("SELECT * FROM Tarea WHERE fecha='"+fecha_sistema+"' AND hora= '"+hora_sistema+"' AND activo=  '" + AC + "' ", null);

                if(fila.moveToFirst()){
                    alarma= fila.getInt(0);
                    titulo=fila.getString(1);
                    HN=fila.getString(5);
                    admin.UpdateActivo(alarma,"0");

                    createNotificationchange(getApplicationContext());
                    triggerNotification(getApplicationContext(),alarma,titulo,HN);
                    config = admin.getConfig("1");

                    while(config.moveToNext()){
                        ST = config.getString(1);
                    }
                    if(ST.equals("on")){

                            startService(new Intent(getApplicationContext(), ServiceBurbuja.class));


                    }

                }
            }
            bd.close();

            con += 1;
            if(con == 100){
               con = 1;
            }
           //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        }
        private void createNotificationchange(Context context){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                CharSequence name = "Noticacion";
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        private void triggerNotification(Context contexto,int NOTIFICACION_ID, String t, String hn) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(contexto.getApplicationContext(),CHANNEL_ID);
            builder.setSmallIcon(R.drawable.ic_event_note_black_24dp);
            builder.setContentTitle("Tarea: " + t);
            builder.setContentText(hn);
            builder.setColor(Color.BLUE);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
            builder.setDefaults(Notification.DEFAULT_SOUND);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(contexto.getApplicationContext());
            notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cent = false;
        }
    }
}
