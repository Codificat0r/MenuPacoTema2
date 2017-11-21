package com.example.menupacotema2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnEjercicio1;
    private Button btnEjercicio2;
    private Button btnEjercicio3;
    private Button btnEjercicio4;
    private Button btnEjercicio5;
    private Button btnEjercicio6;
    private Button btnEjercicio7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        inicializar();
    }

    private void inicializar() {
        btnEjercicio1 = findViewById(R.id.btnEjercicio1);
        btnEjercicio1.setOnClickListener(this);
        btnEjercicio2 = findViewById(R.id.btnEjercicio2);
        btnEjercicio2.setOnClickListener(this);
        btnEjercicio3 = findViewById(R.id.btnEjercicio3);
        btnEjercicio3.setOnClickListener(this);
        btnEjercicio4 = findViewById(R.id.btnEjercicio4);
        btnEjercicio4.setOnClickListener(this);
        btnEjercicio5 = findViewById(R.id.btnEjercicio5);
        btnEjercicio5.setOnClickListener(this);
        btnEjercicio6 = findViewById(R.id.btnEjercicio6);
        btnEjercicio6.setOnClickListener(this);
        btnEjercicio7 = findViewById(R.id.btnEjercicio7);
        btnEjercicio7.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.btnEjercicio1:
                i = new Intent(PrincipalActivity.this, AgendaActivity.class);
                startActivity(i);
                break;
            case R.id.btnEjercicio2:
                i = new Intent(PrincipalActivity.this, AlarmasActivity.class);
                startActivity(i);
                break;
            case R.id.btnEjercicio3:

                break;
            case R.id.btnEjercicio4:
                i = new Intent(PrincipalActivity.this, VerWebActivity.class);
                startActivity(i);
                break;
            case R.id.btnEjercicio5:

                break;
            case R.id.btnEjercicio6:

                break;
            case R.id.btnEjercicio7:

                break;
        }
    }
}
