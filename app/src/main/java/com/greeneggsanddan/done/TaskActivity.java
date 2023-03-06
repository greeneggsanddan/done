package com.greeneggsanddan.done;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.greeneggsanddan.done.Adapter.ToDoAdapter;
import com.greeneggsanddan.done.Model.ToDoModel;
import com.greeneggsanddan.done.Utils.DatabaseHandler;
import com.yalantis.library.Koloda;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private SwipeAdapter adapter;
    private List<ToDoModel>taskList;
    private DatabaseHandler db;
    private List<Integer> list;
    private Koloda koloda;
    private ImageButton toMenuButton;
    private ImageButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        taskList = new ArrayList<>();

        koloda = findViewById(R.id.koloda);
        list = new ArrayList<>();
        adapter = new SwipeAdapter(this, list);
        koloda.setAdapter(adapter);

        //do something to display current task

        toMenuButton = findViewById(R.id.toMenuButton);
        addButton = findViewById(R.id.addButton);

        toMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myInt = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myInt);
            }
        });
    }
}