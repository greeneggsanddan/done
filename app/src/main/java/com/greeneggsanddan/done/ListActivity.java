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
import com.greeneggsanddan.done.Utils.RecyclerViewMargin;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private ImageButton addButton;
    private ImageButton toTaskButton;

    private List<ToDoModel> taskList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        taskList = new ArrayList<>();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        RecyclerViewMargin itemDecorator = new RecyclerViewMargin(-40);
//        tasksRecyclerView.addItemDecoration(itemDecorator);

        tasksAdapter = new ToDoAdapter(db, this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        addButton = findViewById(R.id.addButton);
        toTaskButton = findViewById(R.id.toTaskButton);

        ItemTouchHelper itemTouchHelper =  new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        taskList = db.getAllTasks();
        tasksAdapter.setTasks(taskList);

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
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onDestroy() {
//        db.updateDatabase(taskList);
//        super.onDestroy();
//    }
//
//    @Override
//    public void onPause() {
//        db.updateDatabase(taskList);
//        super.onPause();
//    }

//    @Override
//    public void onResume() {
//        db.updateDatabase(taskList);
//        super.onResume();
//    }
}