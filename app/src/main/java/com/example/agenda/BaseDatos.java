package com.example.agenda;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {
    public  static  final  int DATABASE_VERSION =1;
    public static  final String DATABASE_NAME = "Works.db";

    public BaseDatos(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Agregar codigo SQL
        //Crear un BD

        sqLiteDatabase.execSQL("CREATE TABLE Tarea (" +
                "id_tarea INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre VARCHAR NOT NULL, "+
                "fecha VARCHAR NOT NULL," +
                "FM VARCHAR NOT NULL, "+
                "hora VARCHAR NOT NULL," +
                "HM VARCHAR NOT NULL, " +
                "activo VARCHAR NOT NULL, " +
                "descripcion VARCHAR NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE config (" +
                "id_config INTEGER PRIMARY KEY, " +
                "switch VARCHAR NOT NULL, "+
                "servicioB VARCHAR NOT NULL );");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Actualiza BD
    }


    public void  guardaDatos(String work, String date, String FM, String time, String HM, String AC, String desc){

        getReadableDatabase().execSQL("INSERT INTO Tarea VALUES ("+null+", '"+work+"', '"+date+"', '"+FM+"', '"+time+"', '"+HM+"' , '"+AC+"' ,'"+desc+"');");
    }
    public Cursor getTareas(){
        return getReadableDatabase().query("Tarea", null, null, null, null, null, null);
    }

    public  Cursor getWorkById(String id){
        return  getReadableDatabase().rawQuery("SELECT * " +
                "FROM Tarea " +
                "WHERE id_tarea = " + id + ";", null);
    }

    public void  UpdateWork(String id, String nombre, String date,String FMD, String time, String HMD, String desc){
        getReadableDatabase().execSQL("UPDATE Tarea SET nombre = '"+nombre+"', FM = '"+FMD+"', fecha = '"+date+"', hora = '"+time+"', HM = '"+HMD+"', descripcion = '"+desc+"' WHERE id_tarea = " + id + ";");
    }

    public void  UpdateActivo(int id, String AC){
        getReadableDatabase().execSQL("UPDATE Tarea SET activo = '" + AC + "' WHERE id_tarea = " + id + ";");
    }

    public void DeleteWork(String id){
        SQLiteDatabase db= getWritableDatabase();
        if (db!=null){
            db.execSQL("DELETE FROM Tarea WHERE id_Tarea='"+id+"'");
            db.close();
        }
    }
    public void  UpdateConfig(String id,String CM){

        getReadableDatabase().execSQL("UPDATE config SET switch = '" + CM + "' WHERE id_config = " + id + ";");
    }

    public  Cursor getConfig(String id){

        return  getReadableDatabase().rawQuery("SELECT * " +
                "FROM config " +
                "WHERE id_config = " + id + ";", null);
    }
    public void  guardaConfig(String work, String ST, String SN){

        getReadableDatabase().execSQL("INSERT INTO config VALUES ("+work+", '"+ST+"', '"+SN+"');");
    }


}
