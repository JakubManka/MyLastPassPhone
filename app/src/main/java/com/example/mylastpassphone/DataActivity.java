package com.example.mylastpassphone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataActivity extends AppCompatActivity {

    String url;
    String username;
    String password;
    CheckBox checkBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        checkBox = findViewById(R.id.checkBox);
        TextView urlTextView = findViewById(R.id.url);
        TextView usernameTextView = findViewById(R.id.username);
        TextView passwordTextView = findViewById(R.id.password);
        List<String> dataList = new ArrayList<>();

        url = getIntent().getStringExtra("url");
        username = getIntent().getStringExtra("username");
        password = "123kksksksksks";

        urlTextView.setText(url);
        usernameTextView.setText(username);
        passwordTextView.setText(password);

        dataList.add(url);
        dataList.add(username);
        dataList.add(password);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) passwordTextView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            else passwordTextView.setTransformationMethod(PasswordTransformationMethod.getInstance());

        });


    }


}