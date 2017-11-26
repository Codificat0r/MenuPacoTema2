package com.example.menupacotema2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class VerImagenesActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edtUrl;
    private Button btnDescargar;
    private Button btnSiguiente;
    private Button btnAnterior;
    private ImageView imgvImagen;
    private ArrayList<String> urls;
    private static final String URL = "http://alumno.mobi/~alumno/superior/cruz/enlaces.txt";
    private boolean imagenesDescargadasExito;
    private int imagenActual;
    private static final int MAX_TIMEOUT = 2000;
    private static final int RETRIES = 1;
    private static final int TIMEOUT_BETWEEN_RETRIES = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_imagenes);

        urls = new ArrayList<>();
        imagenesDescargadasExito = false;

        edtUrl = (EditText) findViewById(R.id.edtUrl);
        btnDescargar = (Button) findViewById(R.id.btnDescargar);
        btnDescargar.setOnClickListener(this);
        btnSiguiente = (Button) findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(this);
        btnAnterior = (Button) findViewById(R.id.btnAnterior);
        btnAnterior.setOnClickListener(this);
        imgvImagen = (ImageView) findViewById(R.id.imgvImagen);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDescargar:
                descargarImagenes();
                if (imagenesDescargadasExito) {
                    ponerPrimeraImagen();
                    imagenActual = 0;
                }
                break;
            case R.id.btnSiguiente:
                if (imagenesDescargadasExito) {
                    imagenActual++;
                    Picasso.with(this).load(urls.get(imagenActual % (urls.size() - 1))).into(imgvImagen);
                }
                break;
            case R.id.btnAnterior:
                if (imagenesDescargadasExito) {
                    if (imagenActual > 0) {
                        imagenActual--;
                    } else {
                        imagenActual = urls.size() - 1;
                    }
                    Picasso.with(this).load(urls.get(imagenActual % (urls.size() - 1))).into(imgvImagen);
                }
                break;
        }
    }

    private void descargarImagenes() {
        final ProgressDialog progreso = new ProgressDialog(this);
        final AsyncHttpClient cliente = new AsyncHttpClient();
        //final String[] splittedUrl = edtUrl.getText().toString().split("/");
        //File myfile = new File(getApplicationContext().getFilesDir(), splittedUrl[splittedUrl.length - 1]);
        cliente.setTimeout(MAX_TIMEOUT);
        cliente.setMaxRetriesAndTimeout(RETRIES, TIMEOUT_BETWEEN_RETRIES);

        if (URLUtil.isValidUrl(edtUrl.getText().toString())) {
            cliente.get(edtUrl.getText().toString(), new FileAsyncHttpResponseHandler(this) {

                @Override
                public void onStart() {
                    progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progreso.setMessage("Descargando imágenes...");
                    progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            cliente.cancelAllRequests(true);
                        }
                    });
                    progreso.show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    progreso.dismiss();
                    Toast.makeText(VerImagenesActivity.this, "Fallo al conseguir el archivo de enlaces. Código de error: " + statusCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    String linea;
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);
                        while ((linea = br.readLine()) != null) {
                            urls.add(linea);
                        }
                    } catch (FileNotFoundException e) {
                        Toast.makeText(VerImagenesActivity.this, "No se ha encontrado el archivo", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(VerImagenesActivity.this, "Error de entrada/salida", Toast.LENGTH_SHORT).show();
                    }
                    imagenesDescargadasExito = true;
                    progreso.dismiss();
                }
            });

        } else {
            Toast.makeText(VerImagenesActivity.this, "La URL del fichero no es válida", Toast.LENGTH_SHORT).show();
        }
    }

    private void ponerPrimeraImagen() {
        if (!urls.isEmpty()) {
            Picasso.with(VerImagenesActivity.this).load(urls.get(0)).into(imgvImagen);
        } else {
            Toast.makeText(VerImagenesActivity.this, "No se han podido obtener las URLs del fichero", Toast.LENGTH_SHORT).show();
        }
    }


}
