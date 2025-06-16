package com.example.firstaidapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScenarioStepAdapter extends RecyclerView.Adapter<ScenarioStepAdapter.StepViewHolder> {

    private final List<String> currentSteps;
    private final List<String> originalSteps;

    private final StartDragListener dragStartListener;

    public ScenarioStepAdapter(List<String> steps, StartDragListener dragStartListener) {
        this.originalSteps = new ArrayList<>(steps);
        this.currentSteps = new ArrayList<>(steps);
        this.dragStartListener = dragStartListener;
    }

    public interface StartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scenario_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.stepText.setText(currentSteps.get(position));

        holder.dragHandle.setOnTouchListener((v, event) -> {
            if (dragStartListener != null) {
                dragStartListener.onStartDrag(holder);
            }
            return false;
        });
    }


    @Override
    public int getItemCount() {
        return currentSteps.size();
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        TextView stepText;
        ImageView dragHandle; // add this if using drag & drop library later

        public StepViewHolder(View itemView) {
            super(itemView);
            stepText = itemView.findViewById(R.id.tv_step);
            dragHandle = itemView.findViewById(R.id.drag_handle);

        }
    }


    // 🔁 Reset the steps to a shuffled original
    public void resetOrder() {
        currentSteps.clear();
        currentSteps.addAll(originalSteps);
        Collections.shuffle(currentSteps); // optional
        notifyDataSetChanged();
    }

    // 🎯 Used by ItemTouchHelper to swap items during drag
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition >= currentSteps.size()) return;
        Collections.swap(currentSteps, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    // 🧠 Used in checkAnswer() to compare current order
    public List<String> getCurrentSteps() {
        return currentSteps;
    }
}
