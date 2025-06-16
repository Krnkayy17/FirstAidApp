package com.example.firstaidapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.ModuleOverviewActivity;
import com.example.firstaidapp.R;
import com.example.firstaidapp.models.Module;

import java.util.List;

public class ModuleHomeAdapter extends RecyclerView.Adapter<ModuleHomeAdapter.ViewHolder> {
    private final Context context;
    private final List<Module> moduleList;

    public ModuleHomeAdapter(Context context, List<Module> moduleList) {
        this.context = context;
        this.moduleList = moduleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.module_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Module module = moduleList.get(position);
        holder.tvModuleTitle.setText(module.getModuleName());
        holder.btnStartLearning.setOnClickListener(v -> {
            Intent intent = new Intent(context, ModuleOverviewActivity.class);
            intent.putExtra("MODULE_ID", module.getModuleID());
            intent.putExtra("MODULE_TITLE", module.getModuleName());
            intent.putExtra("MODULE_DESCRIPTION", module.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvModuleTitle;
        Button btnStartLearning;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModuleTitle = itemView.findViewById(R.id.tvModuleTitle);
            btnStartLearning = itemView.findViewById(R.id.btnStartLearning);
        }
    }
}
