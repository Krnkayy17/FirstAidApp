package com.example.firstaidapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.R;

import java.util.List;

public class FunFactAdapter extends RecyclerView.Adapter<FunFactAdapter.ViewHolder> {
    private final List<String> facts;

    public FunFactAdapter(List<String> facts) {
        this.facts = facts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fun_fact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvFunFact.setText(facts.get(position));
    }

    @Override
    public int getItemCount() {
        return facts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFunFact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFunFact = itemView.findViewById(R.id.tvFunFact);
        }
    }
}
