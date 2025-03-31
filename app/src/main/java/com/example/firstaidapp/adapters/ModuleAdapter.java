package com.example.firstaidapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.models.Module;
import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {
    private Context context;
    private List<Module> moduleList;

    public ModuleAdapter(Context context, List<Module> moduleList) {
        this.context = context;
        this.moduleList = moduleList;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.module_item, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module = moduleList.get(position);

        holder.tvModuleName.setText(module.getModuleName());
        holder.tvAccessedDate.setText("Last accessed: " + module.getAccessedDate());
        holder.tvModuleDescription.setText(module.getDescription());
        holder.tvDifficulty.setText("Difficulty: " + module.getDifficultyLevel());
        holder.tvDuration.setText("Duration: " + module.getEstimatedDuration() + " minutes");
        holder.tvAssessments.setText("Assessments: " + module.getTotalAssessments());
        holder.tvCompletionCriteria.setText("Completion Criteria: " + module.getCompletionCriteria());

        // Set button text based on completion status
        String buttonText;
        switch (module.getCompletionStatus()) {
            case "Not Started":
                buttonText = "Start Learning";
                break;
            case "In Progress":
                buttonText = "Continue Learning";
                break;
            case "Completed":
                buttonText = "Finished";
                break;
            default:
                buttonText = "Start Learning";
                break;
        }
        holder.btnContinueModule.setText(buttonText);

        // Button Click Listener
        holder.btnContinueModule.setOnClickListener(v -> {
            // Get current date
            String currentDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date());

            // Initialize database
            ModuleDAO moduleDAO = new ModuleDAO(v.getContext());

            // Update last accessed date
            moduleDAO.updateLastAccessed(module.getModuleID(), currentDate);
            holder.tvAccessedDate.setText("Last accessed: " + currentDate);

            // Update completion status based on current state
            if (module.getCompletionStatus().equals("Not Started")) {
                moduleDAO.updateCompletionStatus(module.getModuleID(), "In Progress");
                holder.btnContinueModule.setText("Continue Learning");
            } else if (module.getCompletionStatus().equals("In Progress")) {
                moduleDAO.updateCompletionStatus(module.getModuleID(), "Completed");
                holder.btnContinueModule.setText("Finished");
            }
        });
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        TextView tvModuleName, tvAccessedDate, tvModuleDescription, tvDifficulty, tvDuration, tvAssessments, tvCompletionCriteria;
        ProgressBar progressBar;
        Button btnContinueModule;

        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModuleName = itemView.findViewById(R.id.tvModuleName);
            tvAccessedDate = itemView.findViewById(R.id.tvAccessedDate);
            tvModuleDescription = itemView.findViewById(R.id.tvModuleDescription);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvAssessments = itemView.findViewById(R.id.tvAssessments);
            tvCompletionCriteria = itemView.findViewById(R.id.tvCompletionCriteria);
            progressBar = itemView.findViewById(R.id.progressBar);
            btnContinueModule = itemView.findViewById(R.id.btnContinueModule);
        }
    }
}