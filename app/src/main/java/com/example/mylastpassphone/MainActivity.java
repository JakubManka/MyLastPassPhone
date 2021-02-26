package com.example.mylastpassphone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.fasterxml.jackson.dataformat.csv;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toolbar;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    Intent myFileIntent;
    ListView listView;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list);
        list = new ArrayList<>();
        list.add("Kuba");
        list.add("duba");
        list.add("mamaa");
        list.add("sdasa");
        list.add("dsdf");
        list.add("zewrw");
        list.add("dsdf");
        list.add("zewrw");
        list.add("dsdf");
        list.add("zewrw");
        list.add("dsdf");
        list.add("zewrw");
        list.add("dsdf");
        list.add("zewrw");
        list.add("dsdf");
        list.add("zewrw");
        list.add("dsdf");
        list.add("zewrw");
        list.add("dsdf");
        list.add("zewrw");
        list.add("dsdf");
        list.add("zewrw");
        list.add("dsdf");
        list.add("zewrw");

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> startActivity(new Intent(MainActivity.this, Data.class)));


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

            myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            myFileIntent.setType("*/*");
            startActivityForResult(myFileIntent, 10);
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            String path = Objects.requireNonNull(data).getData().getPath();

        }
    }

    private void convertToJson(){

    }


}
