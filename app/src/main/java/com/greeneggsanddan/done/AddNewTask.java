package com.greeneggsanddan.done;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.greeneggsanddan.done.Adapter.ToDoAdapter;
import com.greeneggsanddan.done.Model.ToDoModel;
import com.greeneggsanddan.done.Utils.DatabaseHandler;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText;
    private ImageButton newTaskSaveButton;
    private DatabaseHandler db;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); //Allows the BottomSheetDialogFragment to readjust and move upwards when typing

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = getView().findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);
        newTaskText.requestFocus();

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle !=null) {
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                newTaskSaveButton.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")) { //disables save button if no text entered
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray), PorterDuff.Mode.SRC_IN);
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                if(finalIsUpdate) {
                    db.updateTask(bundle.getInt("id"), text);
                } else {
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener) {
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
