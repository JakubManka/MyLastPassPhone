package com.example.mylastpassphone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    Intent myFileIntent;
    ListView listView;
    List<String> list;
    List<String[]> csvData;
    private final int STORAGE_PERMISSION_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list);
        list = new ArrayList<>();
        csvData = new ArrayList<>();
        list.add("SSSSS");

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
//            TextView textView = (TextView) view.findViewById(R.id.list)
            System.out.println("id " + listView.getItemAtPosition(position).toString());
            Intent intent = new Intent(MainActivity.this, Data.class);
            intent.putExtra("url", csvData.get(position+1)[0]);
            intent.putExtra("username", csvData.get(position+1)[1]);
            intent.putExtra("password", csvData.get(position+1)[2]);
            startActivity(new Intent(intent));


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_icon);
        MenuItem addItem = menu.findItem(R.id.add_icon);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("type web name");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return true;
            }
        });

        addItem.setOnMenuItemClickListener(item -> {

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "You have perrmision", Toast.LENGTH_SHORT).show();
            }else {
                requestStoragePermission();
            }

            myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            myFileIntent.setType("*/*");
            startActivityForResult(myFileIntent, 10);
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(this)
                    .setTitle("Persmission needed")
                    .setMessage("This permission is needed to read the csv file")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
       if (requestCode == STORAGE_PERMISSION_CODE) {
           if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
           }else {
               Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
           }
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
//            Uri uri = Objects.requireNonNull(data).getData();
//            File file = new File(uri.getPath());//create path from uri
            String path = "/storage/emulated/0/Download/lastpass_export.csv";
//            String path = file.getAbsolutePath();
//            File downloadFolder = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
//            String path = downloadFolder.getAbsolutePath();
            try {
                setList(path);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void setList(String path) throws IOException {
        String line = "";

        BufferedReader br = new BufferedReader(new FileReader(path));

        while ((line = br.readLine()) != null) {
            if (line.contains("http://")) {
                line = line.replace("http://", "");
            } else if (line.contains("https://")) {
                line = line.replace("https://", "");
            }
                csvData.add(line.split(","));
                list.clear();

                for (int i = 1; i < csvData.size(); i++) {
                    list.add(csvData.get(i)[0]);
                }

            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(arrayAdapter);
        }
        System.out.println(csvData.size());
    }
}
