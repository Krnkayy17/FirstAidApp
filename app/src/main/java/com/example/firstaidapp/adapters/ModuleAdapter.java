package com.example.firstaidapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.ModuleOverviewActivity;
import com.example.firstaidapp.R;
import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.models.Module;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {

    private final Context context;
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

        // Set basic module information
        holder.tvModuleName.setText(module.getModuleName());
        holder.tvAccessedDate.setText(formatAccessedDate(module.getAccessedDate()));
        holder.tvModuleDescription.setText(module.getDescription());
        holder.tvDifficulty.setText("Difficulty: " + module.getDifficultyLevel());
        holder.tvDuration.setText("Duration: " + module.getEstimatedDuration() + " minutes");
        holder.tvAssessments.setText("Assessments: " + module.getTotalAssessments());
        holder.tvCompletionCriteria.setText("Completion: " + module.getCompletionCriteria());

        // Set progress bar and text
        int progress = module.getProgressPercentage();
        holder.progressBar.setProgress(progress);
        holder.tvModuleProgress.setText("Progress: " + progress + "%");

        setStatusUI(holder, module);

        // Handle locked or unlocked state
        if (module.isLocked()) {
            setupLockedState(holder);
        } else {
            setupUnlockedState(holder, module, progress);
        }
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public void setModules(List<Module> updatedModules) {
        this.moduleList = updatedModules;
    }

    private String formatAccessedDate(String date) {
        return (date == null || date.isEmpty()) ? "Not accessed yet" : "Last accessed: " + date;
    }

    // Sets the status UI depending on module's completion status
    private void setStatusUI(ModuleViewHolder holder, Module module) {
        int colorId;
        String statusText;
        String buttonText;

        switch (module.getCompletionStatus()) {
            case "Not Started":
                statusText = "❌ Not Started";
                colorId = R.color.module_not_started;
                buttonText = "Start Learning";
                break;
            case "In Progress":
                statusText = "🕒 In Progress";
                colorId = R.color.module_in_progress;
                buttonText = "Continue Learning";
                break;
            case "Completed":
                statusText = "✅ Completed";
                colorId = R.color.module_completed;
                buttonText = "Finished";
                break;
            default:
                statusText = module.getCompletionStatus();
                colorId = R.color.gray;
                buttonText = "Start";
        }

        holder.tvModuleStatus.setText(statusText);
        holder.tvModuleStatus.setBackgroundColor(ContextCompat.getColor(context, colorId));
        holder.btnContinueModule.setText(buttonText);
    }

    private void setupLockedState(ModuleViewHolder holder) {
        holder.btnContinueModule.setText("Locked 🔒");
        holder.btnContinueModule.setEnabled(false);
        holder.btnContinueModule.setBackgroundColor(ContextCompat.getColor(context, R.color.module_locked));

        // Dim the entire card view
        holder.itemView.setAlpha(0.5f);

        // Show a toast on click
        holder.btnContinueModule.setOnClickListener(v ->
                Toast.makeText(context, "Complete the previous module to unlock this.", Toast.LENGTH_SHORT).show());

        // Tooltip on long press (entire card)
        holder.itemView.setOnLongClickListener(v -> {
            Toast.makeText(context, "Unlock this by completing the previous module.", Toast.LENGTH_LONG).show();
            return true;
        });
    }


    private void setupUnlockedState(ModuleViewHolder holder, Module module, int progress) {
        holder.itemView.setAlpha(1f); // Restore full opacity
        holder.btnContinueModule.setEnabled(true);
        holder.btnContinueModule.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        // Remove long click listener if previously set
        holder.itemView.setOnLongClickListener(null);

        holder.btnContinueModule.setOnClickListener(v -> {
            // Update access date
            String currentDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date());
            ModuleDAO moduleDAO = new ModuleDAO(context);
            moduleDAO.updateLastAccessed(module.getModuleID(), currentDate);
            module.setAccessedDate(currentDate);
            holder.tvAccessedDate.setText("Last accessed: " + currentDate);

            // Set module to "In Progress" if it was "Not Started"
            if ("Not Started".equals(module.getCompletionStatus())) {
                module.setCompletionStatus("In Progress");
                moduleDAO.updateCompletionStatus(module.getModuleID(), "In Progress");
                setStatusUI(holder, module);
            }

            Intent intent = createModuleIntent(module);
            startActivity(intent, true);
        });
    }


    private Intent createModuleIntent(Module module) {
        Intent intent = new Intent(context, ModuleOverviewActivity.class);
        intent.putExtra("MODULE_ID", module.getModuleID());
        intent.putExtra("MODULE_TITLE", module.getModuleName());
        intent.putExtra("MODULE_DESCRIPTION", module.getDescription());

        // Choose image based on module name
        int imageRes;
        switch (module.getModuleName().toLowerCase()) {
            case "cpr":
                imageRes = R.drawable.cpr_image;
                break;
            case "bleeding management":
                imageRes = R.drawable.bleeding_image;
                break;
            default:
                imageRes = R.drawable.ic_modules;
        }
        intent.putExtra("MODULE_IMAGE", imageRes);

        return intent;
    }

    private void startActivity(Intent intent, boolean slide) {
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(
                    slide ? android.R.anim.slide_in_left : android.R.anim.fade_in,
                    slide ? android.R.anim.slide_out_right : android.R.anim.fade_out
            );
        }
    }

    // ViewHolder class that holds views for each module card
    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        TextView tvModuleName, tvAccessedDate, tvModuleDescription, tvDifficulty,
                tvDuration, tvAssessments, tvCompletionCriteria, tvModuleProgress, tvModuleStatus;
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
            tvModuleProgress = itemView.findViewById(R.id.tvModuleProgress);
            tvModuleStatus = itemView.findViewById(R.id.tvModuleStatus);
            progressBar = itemView.findViewById(R.id.progressBar);
            btnContinueModule = itemView.findViewById(R.id.btnContinueModule);
        }
    }
}
