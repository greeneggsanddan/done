package com.greeneggsanddan.done;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.greeneggsanddan.done.Adapter.ToDoAdapter;

import java.util.Collections;

public class RecyclerItemTouchHelper extends ItemTouchHelper.Callback {

    private ToDoAdapter adapter;
    private boolean isItemMoved = false;

    public RecyclerItemTouchHelper(ToDoAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        );
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        isItemMoved = true;
        return true;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        adapter.swapItems(fromPos, toPos);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && isItemMoved) {
            adapter.updateItems();
            isItemMoved = false;
        }
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        switch (direction) {
            case ItemTouchHelper.RIGHT:
                adapter.deleteItem(position);
                break;
            case ItemTouchHelper.LEFT:
                adapter.editItem(position);
                break;
        }
        isItemMoved = false;
    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;
        background = new ColorDrawable(Color.WHITE);

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX>0) {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.outline_delete);
        } else {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.outline_edit);
        }

        int iconMargin = 32 * Math.round(Resources.getSystem().getDisplayMetrics().density); //sets margin in dp
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if(dX>0) { //Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft()
                    + ((int)dX) +backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(c);
        icon.draw(c);
    }
}
