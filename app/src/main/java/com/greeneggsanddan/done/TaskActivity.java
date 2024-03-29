package com.greeneggsanddan.done;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    private List<ToDoModel> todoList;
    private List<ToDoModel> previousTodoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        todoList = new ArrayList<>();

        CardStackView cardStackView = findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
            }

            @Override
            public void onCardSwiped(Direction direction) {
                final int position = manager.getTopPosition() - 1;
                if (direction == Direction.Right || direction == Direction.Left) {
                    adapter.deleteItem(position);
                }
                if (direction == Direction.Top || direction == Direction.Bottom) {
                    adapter.snoozeItem(position);
                    Toast.makeText(TaskActivity.this, "Task snoozed", Toast.LENGTH_SHORT).show();
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
                if (adapter.getItemCount()==2) {
                    manager.setDirections(Direction.HORIZONTAL);
                }
            }

            @Override
            public void onCardDisappeared(View view, int position) {

            }
        });
        manager.setVisibleCount(2);
        manager.setScaleInterval(.95f);
        manager.setDirections(Direction.FREEDOM);
        adapter = new CardStackAdapter(db, this);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
        ImageButton toMenuButton = findViewById(R.id.toMenuButton);
        ImageButton addButton = findViewById(R.id.addButton);

        todoList = db.getAllTasks();
        addLastTask(todoList);
        previousTodoList = todoList;
        adapter.setTasks(todoList);

        toMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myInt = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(myInt);
                overridePendingTransition(R.anim.bounce_in, R.anim.bounce_out);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
                manager.setSwipeableMethod(SwipeableMethod.Manual);
                manager.setDirections(Direction.FREEDOM);
            }
        });
    }
    private void addLastTask (List<ToDoModel> todoList) { //creates the "You are d.one" card at the end of the CardStack
        ToDoModel lastTask = new ToDoModel();
        lastTask.setTask("You are d.one");
        todoList.add(lastTask);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        todoList = db.getAllTasks();
        addLastTask(todoList);
        adapter.setTasks(todoList);
        adapter.notifyDataSetChanged();
        if (todoList.size() != previousTodoList.size()) { //Checks to see if a task was added. This will need to be updated for re-ordered tasks and back button activity
            Toast.makeText(TaskActivity.this, "Task added", Toast.LENGTH_SHORT).show();
            previousTodoList = todoList;
        }
    }

    @Override
    public void onResume() {
        todoList = db.getAllTasks();
        addLastTask(todoList);
        adapter.setTasks(todoList);
        adapter.notifyDataSetChanged();
        super.onResume();
    }

}