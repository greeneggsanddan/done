package com.greeneggsanddan.done;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.greeneggsanddan.done.Adapter.ToDoAdapter;
import com.greeneggsanddan.done.Model.ToDoModel;
import com.greeneggsanddan.done.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements DialogCloseListener {

    private ToDoAdapter adapter;
    private List<ToDoModel> todoList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        todoList = new ArrayList<>();

        RecyclerView tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ToDoAdapter(db, this);
        tasksRecyclerView.setAdapter(adapter);

        ImageButton addButton = findViewById(R.id.addButton);
        ImageButton toTaskButton = findViewById(R.id.toTaskButton);

        ItemTouchHelper itemTouchHelper =  new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        todoList = db.getAllTasks();
        adapter.setTasks(todoList);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        toTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myInt = new Intent(getApplicationContext(), TaskActivity.class);
                startActivity(myInt);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        todoList = db.getAllTasks();
        adapter.setTasks(todoList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        todoList = db.getAllTasks();
        adapter.setTasks(todoList);
        adapter.notifyDataSetChanged();
    }
}