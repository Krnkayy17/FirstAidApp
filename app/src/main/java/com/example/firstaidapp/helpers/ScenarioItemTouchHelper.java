package com.example.firstaidapp.helpers;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.adapters.ScenarioStepAdapter;

public class ScenarioItemTouchHelper extends ItemTouchHelper.Callback {

    private final ScenarioStepAdapter adapter;

    public ScenarioItemTouchHelper(ScenarioStepAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false; // we use the drag handle instead
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false; // no swiping
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(
            RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            RecyclerView.ViewHolder target) {

        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // no swipe
    }
}
