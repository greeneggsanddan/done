package com.greeneggsanddan.done;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import com.greeneggsanddan.done.Adapter.CardStackAdapter;
import com.greeneggsanddan.done.Model.ToDoModel;
import com.greeneggsanddan.done.Utils.DatabaseHandler;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity implements DialogCloseListener {

    private CardStackAdapter adapter;
    private DatabaseHandler db;
    private CardStackLayoutManager manager;
    private CardStackView cardStackView;
    private List<ToDoModel> taskList;
    private ImageButton toMenuButton;
    private ImageButton addButton;
    private List<ToDoModel> previousTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        taskList = new ArrayList<>();

        cardStackView = findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {

            }

            @Override
            public void onCardSwiped(Direction direction) {
                final int position = manager.getTopPosition() - 1;
                if (direction == Direction.Right) {
                    adapter.deleteItem(position);
                }
            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {

            }

            @Override
            public void onCardAppeared(View view, int position) {
                if (adapter.getItemCount()==1) {
                    manager.setSwipeableMethod(SwipeableMethod.None);
                }
            }

            @Override
            public void onCardDisappeared(View view, int position) {

            }
        });
        manager.setVisibleCount(2);
        manager.setScaleInterval(.95f);
        adapter = new CardStackAdapter(db, this);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
        toMenuButton = findViewById(R.id.toMenuButton);
        addButton = findViewById(R.id.addButton);

        taskList = db.getAllTasks();
        previousTaskList = taskList;
        adapter.setTasks(taskList);

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
                manager.setSwipeableMethod(SwipeableMethod.Manual);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        adapter.setTasks(taskList);
        adapter.notifyDataSetChanged();
        if (taskList.size() != previousTaskList.size()) { //Checks to see if a task was added. This will need to be updated for re-ordered tasks and back button activity
            Toast.makeText(TaskActivity.this, "Task added", Toast.LENGTH_SHORT).show();
            previousTaskList = taskList;
        }
    }

}