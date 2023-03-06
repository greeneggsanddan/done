package com.greeneggsanddan.done;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.greeneggsanddan.done.Adapter.SwipeAdapter;
import com.greeneggsanddan.done.Model.ToDoModel;
import com.greeneggsanddan.done.Utils.DatabaseHandler;
import com.yalantis.library.Koloda;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity implements DialogCloseListener {

    private SwipeAdapter adapter;
    private DatabaseHandler db;
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

        koloda = findViewById(R.id.koloda);
        adapter = new SwipeAdapter(this, db);
        koloda.setAdapter(adapter);

        toMenuButton = findViewById(R.id.toMenuButton);
        addButton = findViewById(R.id.addButton);


        toMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myInt = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myInt);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

//    @Override
//    public void handleDialogClose(DialogInterface dialog) {
//        taskList = db.getAllTasks();
//        tasksAdapter.setTasks(taskList);
//        tasksAdapter.notifyDataSetChanged();
//    }
}