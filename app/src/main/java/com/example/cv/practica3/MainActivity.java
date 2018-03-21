package com.example.cv.practica3;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

//ORMLite para base de datos sqlite
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String PREFS = "app.preferences";
    private SharedPreferences prefs;
    EditText etUserName, etTexto;
    CheckBox cbRecordar;
    Button btnEscribir, btnLeer, btnListar;
    private static final int PERM_CODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUserName = (EditText) findViewById(R.id.etUsername);
        cbRecordar = (CheckBox) findViewById(R.id.cbRecordar);
        etTexto = (EditText) findViewById(R.id.etTexto);
        btnEscribir = (Button) findViewById(R.id.btnEscribir);
        btnEscribir.setOnClickListener(this);

        btnLeer = (Button) findViewById(R.id.btnLeer);
        btnLeer.setOnClickListener(this);

        btnListar = (Button) findViewById(R.id.btnListar);
        btnListar.setOnClickListener(this);

        prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        boolean recordar = prefs.getBoolean("recordar", false);
        String userName = prefs.getString("username", null);
        if (recordar) {
            etUserName.setText(userName);
            cbRecordar.setChecked(true);
        } else {
            cbRecordar.setChecked(false);
            etUserName.setText(null);
        }


    }//fin On create


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();


        SharedPreferences.Editor editor = prefs.edit();
        if (cbRecordar.isChecked()) {
            editor.putBoolean("recordar", true);
            editor.putString("username", etUserName.getText().toString());

        } else {
            editor.putBoolean("recordar", false);
            editor.putString("username", null);
        }

        editor.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEscribir:
                verificaPermisoExternalStorage();
                break;
            case R.id.btnLeer:
                leerExterna();
                break;
            case R.id.btnListar:
                listar();
                break;


        }
    }//Fin On Click


    private void escribirInterna() {
        String fileName = "pruebaescritura.txt";
        String texto = etTexto.getText().toString();
        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(texto.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void leerInterna() {
        String fileName = "pruebaescritura.txt";
        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String result = sb.toString();

            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void verificaPermisoExternalStorage() {
            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permissionCheck ==PackageManager.PERMISSION_GRANTED){
                    escribirExterna();
                } else {
                    askForPermission();
                }

    }

    private void askForPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERM_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Tenemos Permiso!  Escribimos", Toast.LENGTH_SHORT).show();
            escribirExterna();
        } else {
            Toast.makeText(this, "No tenemos permiso", Toast.LENGTH_SHORT).show();
        }
    }
    private void escribirExterna(){
        String fileName="pruebaescritura.txt";
        String texto=etTexto.getText().toString();
        File ruta =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(ruta.getAbsolutePath(),fileName);

        try {
            OutputStreamWriter osw=new OutputStreamWriter(new FileOutputStream(file));
            osw.write(texto);
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void leerExterna(){
        String fileName="pruebaescritura.txt";
        String texto=etTexto.getText().toString();
        File ruta =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(ruta.getAbsolutePath(),fileName);

        try {
            BufferedReader osw=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String result=osw.readLine();
            osw.close();
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void listar(){
        Toast.makeText(this, "No Implementado", Toast.LENGTH_SHORT).show();

    }


}//Fin Clase
