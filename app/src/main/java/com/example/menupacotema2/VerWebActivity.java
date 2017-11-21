package com.example.menupacotema2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.StrictMode;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class VerWebActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtUrl;
    private EditText edtNombreFichero;
    private RadioButton rbtnJavaNet;
    private RadioButton rbtnAAHC;
    private RadioButton rbtnVolley;
    private WebView wbvPagina;
    private Button btnConectar;
    private Button btnDescargar;
    private static final int MAX_TIMEOUT = 2000;
    private static final int RETRIES = 1;
    private static final int TIMEOUT_BETWEEN_RETRIES = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_web);

        edtUrl = (EditText) findViewById(R.id.edtUrl);
        edtNombreFichero = (EditText) findViewById(R.id.edtNombreFichero);
        edtNombreFichero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtNombreFichero.setText("");
            }
        });
        rbtnJavaNet = (RadioButton) findViewById(R.id.rbtnJavaNet);
        rbtnAAHC = (RadioButton) findViewById(R.id.rbtnAAHC);
        rbtnVolley = (RadioButton) findViewById(R.id.rbtnVolley);
        wbvPagina = (WebView) findViewById(R.id.wbvPagina);
        btnConectar = (Button) findViewById(R.id.btnConectar);
        btnConectar.setOnClickListener(this);
        btnDescargar = (Button) findViewById(R.id.btnDescargar);
        btnDescargar.setOnClickListener(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    @Override
    public void onClick(View view) {
        if (view == btnConectar) {
            if (rbtnJavaNet.isChecked()) {
                conectarJavaNet();
            }
            if (rbtnAAHC.isChecked()) {
                conectarAAHC();
            }
            if (rbtnVolley.isChecked()) {
                conectarVolley();
            }
        }
        if (view == btnDescargar) {
            if (rbtnJavaNet.isChecked()) {
                descargarJavaNet();
            }
            if (rbtnAAHC.isChecked()) {
                descargarAAHC();
            }
            if (rbtnVolley.isChecked()) {
                descargarVolley();
            }
        }
    }

    public void crearFichero(File file, String contenido) {
        FileOutputStream fos;
        OutputStreamWriter osw;
        BufferedWriter bw;
        try {
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);
            bw.write(contenido);
            bw.close();
            Toast.makeText(this, "Fichero guardado con éxito", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Ha ocurrido un error y no se ha podido crear el fichero", Toast.LENGTH_SHORT).show();
        }
    }

    public void conectarJavaNet() {
        URL url;
        HttpURLConnection huc;
        InputStreamReader isr;
        BufferedReader br;
        String linea;
        int codigoRespuesta;
        String respuesta;
        if (URLUtil.isValidUrl(edtUrl.getText().toString())) {
            try {
                respuesta = "";
                url = new URL(edtUrl.getText().toString());
                huc = (HttpURLConnection) url.openConnection();
                codigoRespuesta = huc.getResponseCode();
                if (codigoRespuesta == HttpURLConnection.HTTP_OK) {
                    isr = new InputStreamReader(huc.getInputStream());
                    br = new BufferedReader(isr);
                    while ((linea = br.readLine()) != null) {
                        respuesta += linea;
                    }
                    wbvPagina.loadData(respuesta, "text/html", "UTF-8");
                } else {
                    Toast.makeText(this, "La web no pudo ser accedida: " + codigoRespuesta, Toast.LENGTH_SHORT).show();
                }
            } catch (MalformedURLException e) {
                Toast.makeText(this, "La URL no es correcta", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "La URL no es correcta", Toast.LENGTH_SHORT).show();
        }
    }

    public void descargarJavaNet() {
        URL url;
        HttpURLConnection huc;
        InputStreamReader isr;
        BufferedReader br;
        String linea;
        int codigoRespuesta;
        String respuesta;
        if (URLUtil.isValidUrl(edtUrl.getText().toString())) {
            try {
                respuesta = "";
                url = new URL(edtUrl.getText().toString());
                huc = (HttpURLConnection) url.openConnection();
                codigoRespuesta = huc.getResponseCode();
                if (codigoRespuesta == HttpURLConnection.HTTP_OK) {
                    isr = new InputStreamReader(huc.getInputStream());
                    br = new BufferedReader(isr);
                    while ((linea = br.readLine()) != null) {
                        respuesta += linea;
                    }
                    crearFichero(new File(Environment.getExternalStorageDirectory(), edtNombreFichero.getText().toString()), respuesta);
                } else {
                    Toast.makeText(this, "La web no pudo ser accedida: " + codigoRespuesta, Toast.LENGTH_SHORT).show();
                }
            } catch (MalformedURLException e) {
                Toast.makeText(this, "La URL no es correcta", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "La URL no es correcta", Toast.LENGTH_SHORT).show();
        }
    }

    public void conectarAAHC() {
        final ProgressDialog progress = new ProgressDialog(this);
        final AsyncHttpClient ahc = new AsyncHttpClient();
        ahc.setTimeout(MAX_TIMEOUT);
        ahc.setMaxRetriesAndTimeout(RETRIES, TIMEOUT_BETWEEN_RETRIES);
        ahc.get(edtUrl.getText().toString(), new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setMessage("Cargando página...");
                progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        ahc.cancelAllRequests(true);
                        progress.dismiss();
                    }
                });
                progress.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.dismiss();
                Toast.makeText(VerWebActivity.this, "Código de error: " + statusCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                wbvPagina.loadData(responseString, "text/html", "UTF-8");
                progress.dismiss();
            }

        });

    }

    public void descargarAAHC() {
        final ProgressDialog progress = new ProgressDialog(this);
        final AsyncHttpClient ahc = new AsyncHttpClient();
        ahc.setTimeout(MAX_TIMEOUT);
        ahc.setMaxRetriesAndTimeout(RETRIES, TIMEOUT_BETWEEN_RETRIES);
        ahc.get(edtUrl.getText().toString(), new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setMessage("Descargando página a memoria externa...");
                progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        ahc.cancelAllRequests(true);
                        progress.dismiss();
                    }
                });
                progress.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.dismiss();
                Toast.makeText(VerWebActivity.this, "Código de error: " + statusCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                crearFichero(new File(Environment.getExternalStorageDirectory(), edtNombreFichero.getText().toString()), responseString);
                progress.dismiss();
            }

        });
    }

    public void conectarVolley() {
        RequestQueue rq = Volley.newRequestQueue(this);

        StringRequest sr = new StringRequest(StringRequest.Method.GET, edtUrl.getText().toString(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        wbvPagina.loadData(response, "text/html", "UTF-8");
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerWebActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        rq.add(sr);
    }

    public void descargarVolley() {
        RequestQueue rq = Volley.newRequestQueue(this);

        StringRequest sr = new StringRequest(StringRequest.Method.GET, edtUrl.getText().toString(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        crearFichero(new File(Environment.getExternalStorageDirectory(), edtNombreFichero.getText().toString()), response);
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerWebActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        rq.add(sr);
    }
}
