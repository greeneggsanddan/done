package com.greeneggsanddan.done.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.greeneggsanddan.done.Model.ToDoModel;
import com.greeneggsanddan.done.R;
import com.greeneggsanddan.done.Utils.DatabaseHandler;

import java.util.List;

public class SwipeAdapter extends BaseAdapter {
    private Context context;
    private DatabaseHandler db;
    private List<ToDoModel> todoList;

    public SwipeAdapter(Context context, DatabaseHandler db) {
        this.context = context;
        this.db = db;
    }

    @Override
    public int getCount() {
        return todoList.size();
    }

    @Override
    public Object getItem(int position) {
        return todoList.get(position);
    }

    @Override
    public long getItemId(int position) { //this might be wrong
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_koloda, parent, false);
        }
        ((TextView) v.findViewById(R.id.currentTaskText)).setText(todoList.get(position).getTask());
        return v;
    }

}
