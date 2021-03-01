package com.example.mylastpassphone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataActivity extends AppCompatActivity {

    ArrayAdapter<String> dataArrayAdapter;
    String url;
    String username;
    String password;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        ListView dataListView = findViewById(R.id.dataList);
        List<String> dataList = new ArrayList<>();



        url = "url: " + getIntent().getStringExtra("url");
        username = "username: " + getIntent().getStringExtra("username");
        password = "password: " + "123kksksksksks";

        dataList.add(url);
        dataList.add(username);
        dataList.add(password);

        dataArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        dataListView.setAdapter(dataArrayAdapter);


    }


}