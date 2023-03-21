package com.greeneggsanddan.done.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.greeneggsanddan.done.AddNewTask;
import com.greeneggsanddan.done.ListActivity;
import com.greeneggsanddan.done.Model.ToDoModel;
import com.greeneggsanddan.done.R;
import com.greeneggsanddan.done.Utils.DatabaseHandler;

import java.util.Collections;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private ListActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db, ListActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        db.openDatabase();
        ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
    }

    public int getItemCount(){
        return todoList.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return activity;
    }

    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle =new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public void swapItems(int draggedItemIndex, int targetIndex) {
        Collections.swap(todoList, draggedItemIndex, targetIndex);
        notifyItemMoved(draggedItemIndex, targetIndex);
    }

    public void updateItems() {
        db.updateDatabase(todoList);
        todoList = db.getAllTasks();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView task;
        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.taskCard);
        }
    }
}
