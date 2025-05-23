package com.example.firstaidapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class ScenarioStepAdapter extends RecyclerView.Adapter<ScenarioStepAdapter.StepViewHolder> {

    private List<String> stepList;

    public ScenarioStepAdapter(List<String> stepList) {
        this.stepList = stepList;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scenario_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.tvStep.setText(stepList.get(position));
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public void onItemMove(int fromPos, int toPos) {
        Collections.swap(stepList, fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
    }

    public List<String> getCurrentOrder() {
        return stepList;
    }

    static class StepViewHolder extends RecyclerView.ViewHolder {
        TextView tvStep;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStep = itemView.findViewById(R.id.tv_step);
        }
    }
}
