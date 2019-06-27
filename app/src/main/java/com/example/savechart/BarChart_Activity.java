package com.example.savechart;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class BarChart_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        BarChart mpBarChart = findViewById(R.id.mp_BarChart);

        setSupportActionBar(toolbar);


        mpBarChart = createBarChar(mpBarChart, dataVallues1());
        final BarChart finalMpBarChart = mpBarChart;
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    saveImage(finalMpBarChart, "provaBarChat");
                    Snackbar.make(view, "Chart saved in your gallery", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Log.i("ERR", "NO PERMISION");
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Valori Che inserisco nel Grafico

    // i valori di questo chart sono differeneti dal line chart perchè sono di tipi diversi, ancora
    // non sono riuscito a convertirli
    private ArrayList<BarEntry> dataVallues1() {

        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(0, 67));
        dataVals.add(new BarEntry(1, 32));
        dataVals.add(new BarEntry(2, 68));
        dataVals.add(new BarEntry(3, 44));
        dataVals.add(new BarEntry(4, 42));

        return dataVals;
    }

    // funzione che prende in ingresso un oggetto BarChart vuoto e una lista di BarEntry

    private BarChart createBarChar(BarChart bChart, ArrayList<BarEntry> dataVal1) {

        // Cero e imposto il set di dati che inserisco nel grafico a barre
        BarDataSet dataBarSet1 = new BarDataSet(dataVal1, "Val1");

        // imposto il colore
        dataBarSet1.setColor(Color.BLUE);


        BarData bar_Data = new BarData(); //creso il data Base per i dati
        bar_Data.addDataSet(dataBarSet1); //aggiungo il set di dati a data base

        bChart.setData(bar_Data); //aggiungo il database
        bChart.invalidate(); // chiudo il grafino così che non posso apportare modiche
        // bChart.animateX(40);

        return bChart; // restituisco il grafivo
    }


    private void saveImage(BarChart chart, String image_name) {

        //FUNZIONA NON CHIEDETE COME
        //so che creo un oggetto bitmap dove salvare i dati del grafico
        //ingresso gli ho messo il brafico che va salvato
        // e una stringa che dovrà essere poi il nome dell'argomento di cui faccianmo il grafico
        // se il file esiste gia NON LO SOVRASCRIVE
        // QUINDI TOCCA METTERE UN IF EXSIST DELETE

        Bitmap finalBitmap;

        int width = chart.getWidth();
        int height = chart.getHeight();
        Bitmap cBitmap = chart.getChartBitmap();
        finalBitmap = Bitmap.createScaledBitmap(cBitmap, width, height, true);

        // For save file in internal directpory
        String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/";
        String fname = "Bar chart of-" + image_name + ".png";
        File file = new File(path, fname);
        Log.i("LOAD", path + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            // Funzione che crea l'iimmagine
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
