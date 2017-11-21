package com.example.menupacotema2;

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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

public class AgendaActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnGuardar;
    private Button btnCargar;
    private TextView txvListaContactos;
    private EditText edtNombre;
    private EditText edtTelefono;
    private EditText edtEmail;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        inicializar();
    }

    private void inicializar() {
        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(this);
        btnCargar = findViewById(R.id.btnCargar);
        btnCargar.setOnClickListener(this);
        txvListaContactos = findViewById(R.id.txvListaContactos);
        edtNombre = findViewById(R.id.edtNombre);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtEmail = findViewById(R.id.edtEmail);

        file = new File (getApplicationContext().getFilesDir(), "contactos.txt");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGuardar:
                guardarContacto();
                break;
            case R.id.btnCargar:
                leerContactos();
                break;
        }
    }

    private boolean comprobarCamposLlenos() {
        if (edtNombre.getText().toString().trim().length() != 0)
            if (edtTelefono.getText().toString().trim().length() != 0)
                if (edtEmail.getText().toString().trim().length() != 0)
                    return true;
        return false;
    }

    private void guardarContacto() {
        FileOutputStream fos;
        OutputStreamWriter osw;
        BufferedWriter bw;
        if (comprobarCamposLlenos()) {
            try {
                if (!file.exists())
                    file.createNewFile();
                fos = new FileOutputStream(file, true);
                osw = new OutputStreamWriter(fos);
                bw = new BufferedWriter(osw);
                bw.write(edtNombre.getText().toString() + "," + edtTelefono.getText().toString() + "," + edtEmail.getText().toString());
                bw.newLine();
                Toast.makeText(this, "Contacto guardado con éxito.", Toast.LENGTH_SHORT).show();
                //Hay que cerrar el BufferedWriter, no el flujo principal
                bw.close();
            } catch (Exception e) {
                Toast.makeText(this, "Ha ocurrido un problema.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Se deben de rellenar todos los campos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void leerContactos() {
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader br;
        String lineaLeida;
        String[] datos;
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            //Limpiamos el TextView antes de introducir la nueva lista.
            txvListaContactos.setText("");
            while ((lineaLeida = br.readLine()) != null) {
                datos = lineaLeida.split(",");
                txvListaContactos.setText(txvListaContactos.getText().toString() + "Nombre: " + datos[0] + "\n");
                txvListaContactos.setText(txvListaContactos.getText().toString() + "Teléfono: " + datos[1] + "\n");
                txvListaContactos.setText(txvListaContactos.getText().toString() + "Email: " + datos[2] + "\n");
                txvListaContactos.setText(txvListaContactos.getText().toString() + "-------------------" + "\n");
            }
            br.close();
        } catch (FileNotFoundException e){
            Toast.makeText(this, "No existe un fichero de contactos todavía.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Ha ocurrido un problema.", Toast.LENGTH_SHORT).show();
        }

    }
}
