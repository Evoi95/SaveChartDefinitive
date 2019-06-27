package com.example.savechart;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
//chart import
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    // implementeare IF exist delete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        Button makeBarChart = findViewById(R.id.makeBarChart);

        makeBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });

        final LineChart mpLineChart = createLineChart(dataVallues1(), dataVallues2());

        // "Inizio Bottone Per Salvataggio Grafico"
        fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                  saveImage(mpLineChart, "prova2");
                  Snackbar.make(view, "Chart saved in your gallery", Snackbar.LENGTH_LONG)
                          .setAction("Action", null).show();
              }
              else{
                  if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                      Log.i("ERR","NO PERMISION");
                  }
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
              }
          }
        });

        // Fine "Bottone Per Salvataggio Grafico"
    }

    // INIZIO "non la uso"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Button makeBarChart = findViewById(R.id.action_settings);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        }
        return true;
    }
    // Fine "NON LA USO"
    //creazione e salvataggio del lineChart

    private ArrayList<Entry> dataVallues1() {

        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(0, 20));
        dataVals.add(new Entry(1, 22));
        dataVals.add(new Entry(2, 34));
        dataVals.add(new Entry(3, 45));
        dataVals.add(new Entry(4, 21));
        dataVals.add(new Entry(5, 2));
        return dataVals;
    }

    private ArrayList<Entry> dataVallues2() {

        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(0, 12));
        dataVals.add(new Entry(1, 213));
        dataVals.add(new Entry(2, 31));
        dataVals.add(new Entry(3, 41));
        dataVals.add(new Entry(4, 245));
        dataVals.add(new Entry(5, 21));
        return dataVals;
    }

    private LineChart createLineChart(ArrayList<Entry> dataVal1, ArrayList<Entry> dataVal2) {

        LineDataSet lineDataSet1 = new LineDataSet(dataVal1, "Data Set 1");
        LineDataSet lineDataSet2 = new LineDataSet(dataVal2, "Data Set 2");

        final LineChart mpLineChart;
        mpLineChart = findViewById(R.id.line_char);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);

        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();

        return mpLineChart;
    }

    private void saveImage(LineChart chart, String image_name) {

        // Permission is not granted

        Bitmap finalBitmap;
        int width = chart.getWidth();
        int height = chart.getHeight();
        Bitmap cBitmap = chart.getChartBitmap();
        finalBitmap = Bitmap.createScaledBitmap(cBitmap, width, height, true);
        //File path =  getApplicationContext().getFilesDir();
        // For save file in internal directpory
        String path = Environment.getExternalStorageDirectory()+"/"+ Environment.DIRECTORY_DCIM+"/";
        String fname = "Image-" + image_name + ".png";
        File file = new File(path, fname);
        Log.i("LOAD", path + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            out.flush();
            out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void openActivity() {
        Intent intent = new Intent(this, BarChart_Activity.class);
        startActivity(intent);
    }
}
