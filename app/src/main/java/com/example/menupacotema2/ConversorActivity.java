package com.example.menupacotema2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.Header;

public class ConversorActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnConvertir;
    RadioButton rbtnDolarEuro;
    RadioButton rbtnEuroDolar;
    EditText edtxtDolar;
    EditText edtxtEuro;
    private double valorConversion;
    private static final String URLCONVERSION = "http://alumno.mobi/~alumno/superior/cruz/conversion.txt";
    private static final int MAX_TIMEOUT = 2000;
    private static final int RETRIES = 1;
    private static final int TIMEOUT_BETWEEN_RETRIES = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversor);

        obtenerConversion();

        edtxtEuro = (EditText)findViewById(R.id.edtxtEuro);
        edtxtDolar = (EditText)findViewById(R.id.edtxtDolar);
        rbtnDolarEuro = (RadioButton)findViewById(R.id.rbtnDolarEuro);
        rbtnEuroDolar = (RadioButton)findViewById(R.id.rbtnEuroDolar);
        btnConvertir = (Button)findViewById(R.id.btnConvertir);

        btnConvertir.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == btnConvertir)
        {
            try {
                if (rbtnDolarEuro.isChecked()) {
                    edtxtEuro.setText(Double.toString(Double.parseDouble(edtxtDolar.getText().toString()) * valorConversion));
                }
                if (rbtnEuroDolar.isChecked()) {
                    edtxtDolar.setText(Double.toString(Double.parseDouble(edtxtEuro.getText().toString()) / valorConversion));
                }
            }
            catch (Exception e) {
                Toast.makeText(this, "Error en el formato de los números introducidos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void obtenerConversion() {
        final ProgressDialog progreso = new ProgressDialog(this);
        final AsyncHttpClient cliente = new AsyncHttpClient();
        cliente.setTimeout(MAX_TIMEOUT);
        cliente.setMaxRetriesAndTimeout(RETRIES, TIMEOUT_BETWEEN_RETRIES);

        cliente.get(URLCONVERSION, new FileAsyncHttpResponseHandler(this) {

            @Override
            public void onStart() {
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Obteniendo datos necesarios...");
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
                Toast.makeText(ConversorActivity.this, "No se ha podido obtener el valor del cambio", Toast.LENGTH_SHORT).show();
                valorConversion = 0.0;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    valorConversion = Double.parseDouble(br.readLine());
                } catch (FileNotFoundException e) {
                    Toast.makeText(ConversorActivity.this, "No se ha encontrado el archivo en la red", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(ConversorActivity.this, "Error de entrada/salida", Toast.LENGTH_SHORT).show();
                }
                progreso.dismiss();
            }
        });
    }
}
