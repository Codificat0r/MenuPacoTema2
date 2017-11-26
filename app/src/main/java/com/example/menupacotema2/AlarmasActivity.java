package com.example.menupacotema2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AlarmasActivity extends AppCompatActivity {
    private EditText edtTiempo1;
    private EditText edtTiempo2;
    private EditText edtTiempo3;
    private EditText edtTiempo4;
    private EditText edtTiempo5;
    private EditText edtTexto1;
    private EditText edtTexto2;
    private EditText edtTexto3;
    private EditText edtTexto4;
    private EditText edtTexto5;
    private Button btnProgramar;
    private TextView txvSiguienteAlarma;
    private TextView txvMensajeAlarma;
    //Para leer las alarmas
    private int turno;
    String[] splittedLine;
    MediaPlayer mp;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmas);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        }

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        turno = 1;
        file = new File(Environment.getExternalStorageDirectory(), "alarmas.txt");

        edtTiempo1 = (EditText) findViewById(R.id.edtTiempo1);
        edtTiempo2 = (EditText) findViewById(R.id.edtTiempo2);
        edtTiempo3 = (EditText) findViewById(R.id.edtTiempo3);
        edtTiempo4 = (EditText) findViewById(R.id.edtTiempo4);
        edtTiempo5 = (EditText) findViewById(R.id.edtTiempo5);
        edtTexto1 = (EditText) findViewById(R.id.edtTexto1);
        edtTexto2 = (EditText) findViewById(R.id.edtTexto2);
        edtTexto3 = (EditText) findViewById(R.id.edtTexto3);
        edtTexto4 = (EditText) findViewById(R.id.edtTexto4);
        edtTexto5 = (EditText) findViewById(R.id.edtTexto5);
        txvSiguienteAlarma = (TextView) findViewById(R.id.txvSiguienteAlarma);
        txvMensajeAlarma = (TextView) findViewById(R.id.txvMensajeAlarma);
        btnProgramar = (Button) findViewById(R.id.btnProgramar);
        btnProgramar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarAlarmas();
                cargarAlarmas();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }

            case 1: {

                if (grantResults.length > 1
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }

        }
    }

    public boolean[] comprobarLlenos() {
        boolean[] llenos = {false, false, false, false, false};
        if (!edtTiempo1.getText().toString().trim().isEmpty() && !edtTexto1.getText().toString().trim().isEmpty())
            llenos[0] = true;
        if (!edtTiempo2.getText().toString().trim().isEmpty() && !edtTexto2.getText().toString().trim().isEmpty())
            llenos[1] = true;
        if (!edtTiempo3.getText().toString().trim().isEmpty() && !edtTexto3.getText().toString().trim().isEmpty())
            llenos[2] = true;
        if (!edtTiempo4.getText().toString().trim().isEmpty() && !edtTexto4.getText().toString().trim().isEmpty())
            llenos[3] = true;
        if (!edtTiempo5.getText().toString().trim().isEmpty() && !edtTexto5.getText().toString().trim().isEmpty())
            llenos[4] = true;
        return llenos;
    }

    public void guardarAlarmas() {
        boolean[] llenos = comprobarLlenos();
        FileOutputStream fos;
        OutputStreamWriter osw;
        BufferedWriter bw;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                osw = new OutputStreamWriter(fos);
                bw = new BufferedWriter(osw);
                //Recreamos el archivo con las nuevas alarmas
                if (file.exists()) {
                    file.delete();
                    file.createNewFile();
                }
                if (llenos[0]) {
                    bw.append(edtTiempo1.getText().toString() + "," + edtTexto1.getText().toString());
                }
                if (llenos[1]) {
                    bw.newLine();
                    bw.append(edtTiempo2.getText().toString() + "," + edtTexto2.getText().toString());
                }
                if (llenos[2]) {
                    bw.newLine();
                    bw.append(edtTiempo3.getText().toString() + "," + edtTexto3.getText().toString());
                }
                if (llenos[3]) {
                    bw.newLine();
                    bw.append(edtTiempo4.getText().toString() + "," + edtTexto4.getText().toString());
                }
                if (llenos[4]) {
                    bw.newLine();
                    bw.append(edtTiempo5.getText().toString() + "," + edtTexto5.getText().toString());
                }
                bw.close();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "No se ha encontrado el archivo de alarmas", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Error al intentar escribir en el fichero de alarmas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void cargarAlarmas() {
        boolean[] llenos = comprobarLlenos();
        FileInputStream fis;
        InputStreamReader isr;
        String linea;
        BufferedReader br;
        CountDownTimer cdt;
        //Comprobamos que almenos uno esté lleno
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (llenos[0] || llenos[1] || llenos[2] || llenos[3] || llenos[4]) {
                try {
                    fis = new FileInputStream(file);
                    isr = new InputStreamReader(fis);
                    br = new BufferedReader(isr);
                    mp = MediaPlayer.create(this, R.raw.horn);
                    while ((linea = br.readLine()) != null) {
                        splittedLine = linea.split(",");
                        cdt = new CountDownTimer(pasarAMillis(Integer.parseInt(splittedLine[0])), 1000) {
                            @Override
                            public void onTick(long l) {
                                txvSiguienteAlarma.setText("A la alarma " + turno + " le quedan " + pasarAMinutos(l) + " minutos para sonar");
                            }

                            @Override
                            public void onFinish() {
                                txvMensajeAlarma.setText(splittedLine[1]);
                                mp.start();
                            }
                        };
                        cdt.start();
                        turno++;
                        //Espera a que termine la cuenta
                        Thread.sleep(pasarAMillis(Integer.parseInt(splittedLine[0])));
                    }
                    turno = 1;
                    br.close();
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "No se ha encontrado el archivo de alarmas", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(this, "Error al intentar leer en el fichero de alarmas", Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "¡Debe rellenar almenos una alarma!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No hay ninguna tarjeta externa", Toast.LENGTH_SHORT).show();
        }
    }

    public long pasarAMillis(int minutos) {
        return minutos * 60 * 1000;
    }

    public int pasarAMinutos (long millis) {
        return (int)(millis / 60) / 1000;
    }
}
