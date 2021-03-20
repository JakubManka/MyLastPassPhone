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
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    Intent myFileIntent;
    ListView listView;
    List<String> urlList; //list contains the name of the websites
    List<Data> csvData;
    private final int STORAGE_PERMISSION_CODE = 1;
    private static final String FILE_NAME = "csvData.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list);
        urlList = new ArrayList<>();
        csvData = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, urlList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {  // After clicking the url Activity is changed
            System.out.println("id " + listView.getItemAtPosition(position).toString());
            Intent intent = new Intent(MainActivity.this, DataActivity.class);
            Data correctData = csvData.stream()
                    .filter(data -> data.getUrl().equals(listView.getItemAtPosition(position).toString()))
                    .findFirst()
                    .get();

            intent.putExtra("url", correctData.getUrl());
            intent.putExtra("username", correctData.getUsername());
            intent.putExtra("password", correctData.getPassword());

            startActivity(new Intent(intent));
        });

        try {
            Load();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                urlList.clear();
                for(Data data: dataFilter(newText)) {
                    urlList.add(data.getUrl());
                }
                listView.setAdapter(arrayAdapter);
                return true;
            }
        });

        addItem.setOnMenuItemClickListener(item -> {

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "You have perrmision", Toast.LENGTH_SHORT).show();
            } else {
                requestStoragePermission();
            }

            myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            myFileIntent.setType("*/*");
            startActivityForResult(myFileIntent, 10);
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Persmission needed")
                    .setMessage("This permission is needed to read the csv file")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            assert data != null;
            Uri uri = data.getData();
            File file;
            String path = "";
//            Uri uri = Objects.requireNonNull(data).getData();
//            File file = new File(getPath(uri));
//            String path = "/storage/emulated/0/Download/lastpass_export.csv";
//            String path = file.getPath();
//            String path = getRealPathFromURI(uri);

            try {
                file = FileUtil.from(MainActivity.this,uri);
                Log.d("file", "File...:::: uti - "+file .getPath()+" file -" + file + " : " + file .exists());
               path = file.getPath();

            } catch (IOException e) {
                e.printStackTrace();
            }






            System.out.println("sciezka : "  + path);
            try {
                setUrlList(path);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void setUrlList(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        csvRead(br);
        Save();
    }

    public void Save() throws IOException {  // save to file
        StringBuilder s = new StringBuilder();

        for (Data data : csvData) {
            s.append(data.getUrl()).append(",").append(data.getUsername()).append(",").append(data.getPassword()).append("\n");
        }
        try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE)) {
            fos.write(s.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Load() throws IOException { // load from file
        FileInputStream fis = null;

        fis = openFileInput(FILE_NAME);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        csvRead(br);
        fis.close();
    }

    public void csvRead(BufferedReader br) throws IOException { // read the csv file
        urlList.clear();
        csvData.clear();
        String line = "";
        int id = 0;
        while ((line = br.readLine()) != null) {
            String[] s = line.split(",");
            if(s.length >= 3){
                csvData.add(new Data(s[0], s[1], s[2], id));

                id++;
            }
        }

        for (int i = 0; i < csvData.size(); i++) {
            urlList.add(csvData.get(i).getUrl());
        }
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, urlList);
        listView.setAdapter(arrayAdapter);
    }

    private List<Data> dataFilter (CharSequence constraint){
        List<Data> suggestions = new ArrayList<>();

        if(constraint == null || constraint.length() == 0){
            suggestions.addAll(csvData);
        }else {
            String filterPattern = constraint.toString().toLowerCase().trim();

            for (Data data: csvData){;
                if (data.getUrl().toLowerCase().contains(filterPattern)){
                    suggestions.add(data);
                }
            }
        }

        return suggestions;
    }

}
