package com.example.menupacotema2;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class LectivosActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private EditText edtFechaInicio;
    private EditText edtFechaFin;
    private EditText edtFechas;
    private Date fechaCalendario;
    private Date fechaInicio;
    private Date fechaFin;
    private DatePickerDialog datePickerFechaInicio;
    private DatePickerDialog datePickerFechaFin;
    private Button btnCalcular;
    private ArrayList<Date> margenDeFechas;
    private File fechas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dias_lectivos);

        File file_sd = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        new File(file_sd.getAbsolutePath(), "fechas.txt").delete();
        fechas = new File(file_sd.getAbsolutePath(), "fechas.txt");

        btnCalcular = (Button)findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(this);
        edtFechaInicio = (EditText)findViewById(R.id.edtFechaInicio);
        edtFechaInicio.setOnClickListener(this);
        edtFechaFin = (EditText)findViewById(R.id.edtFechaFin);
        edtFechaFin.setOnClickListener(this);
        edtFechas = (EditText)findViewById(R.id.edtFechas);

        try {
            crearFichero(fechas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == edtFechaInicio){
            Calendar calendar = new GregorianCalendar();
            datePickerFechaInicio = DatePickerDialog.newInstance(
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerFechaInicio.show(getFragmentManager(), "Datepickerdialog");
        }

        if(v== edtFechaFin){
            Calendar calendar = new GregorianCalendar();
            datePickerFechaFin = DatePickerDialog.newInstance(
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerFechaFin.show(getFragmentManager(), "Datepickerdialog");
        }

        if(v== btnCalcular) {

            try {
                margenDeFechas = margenDeFechas(fechaInicio, fechaFin);
                if(fechaInicio == null || fechaFin == null)
                    Toast.makeText(this,"Debes introducir las dos fechas",Toast.LENGTH_LONG).show();
                else if(margenDeFechas.size() == 0){
                    Toast.makeText(this,"El margen de fechas no es válido",Toast.LENGTH_LONG).show();

                }
                else{
                    String texto = "";
                    compararConFechasFichero(fechas, margenDeFechas);
                    for (int i = 0; i < margenDeFechas.size(); i++) {
                        texto += (new SimpleDateFormat("dd-MM-yyyy").format(margenDeFechas.get(i))+"\n");
                    }
                    edtFechas.setText(texto);
                }
            } catch (IOException e) {
                Toast.makeText(this,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            } catch (ParseException e) {
                Toast.makeText(this,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            } catch (NullPointerException e){
                Toast.makeText(this,"Debes introducir un margen de fechas",Toast.LENGTH_LONG).show();
            } catch (Exception e){
                Toast.makeText(this,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        fechaCalendario = new Date(year,monthOfYear,dayOfMonth);
        if(view == datePickerFechaInicio){
            fechaInicio = fechaCalendario;
            edtFechaInicio.setText(new SimpleDateFormat("dd-MM-yyyy").format(fechaInicio));
        }
        if(view == datePickerFechaFin){
            fechaFin = fechaCalendario;
            edtFechaFin.setText(new SimpleDateFormat("dd-MM-yyyy").format(fechaFin));
        }
    }

    public void crearFichero(File archivoFechas) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivoFechas,true)));

        bw.write("02-01-2017\n06-01-2017\n28-02-2017\n13-04-2017\n14-04-2017\n15-08-2017" +
                     "\n19-08-2017\n08-09-2017\n12-10-2017\n01-11-2017\n06-12-2017\n08-12-2017" +
                     "\n25-12-2017\n");

        bw.close();
    }

    public ArrayList<Date> margenDeFechas(Date fechaInicio,Date fechaFin){
        ArrayList<Date> margenFechas = new ArrayList<Date>();
        Calendar calendarFComp = new GregorianCalendar();
        Calendar calendarFFin = new GregorianCalendar();

        Date fechaComp = fechaInicio;
        calendarFComp.set(fechaComp.getYear(),fechaComp.getMonth(),fechaComp.getDate());
        calendarFFin.set(fechaFin.getYear(),fechaFin.getMonth(),fechaFin.getDate());

        while (calendarFComp.compareTo(calendarFFin) <= 0){
            margenFechas.add(fechaComp);
            calendarFComp.add(Calendar.DAY_OF_YEAR,1);
            fechaComp = calendarFComp.getTime();
        }

        return margenFechas;
    }

    public void compararConFechasFichero(File archivoFechas, ArrayList<Date> margenDeFechas) throws IOException, ParseException {

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivoFechas)));

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        String linea;

        while((linea = br.readLine())!=null){
            Date fechaFes = sdf.parse(linea);

            for (int i = 0; i < margenDeFechas.size(); i++) {
                Date fechaComp = sdf.parse(new SimpleDateFormat("dd-MM-yyyy").format(margenDeFechas.get(i)));
                if(fechaComp.equals(fechaFes))
                    margenDeFechas.remove(i);
            }
        }

        br.close();
    }
}
