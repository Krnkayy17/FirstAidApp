package com.example.firstaidapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.ModuleProgressDetailActivity;
import com.example.firstaidapp.R;
import com.example.firstaidapp.database.AssessmentResultDAO;
import com.example.firstaidapp.database.ContentDAO;
import com.example.firstaidapp.database.UserContentViewDAO;
import com.example.firstaidapp.models.AssessmentResult;
import com.example.firstaidapp.models.Module;
import com.example.firstaidapp.utils.SessionManager;

import java.util.List;

public class ProgressModuleAdapter extends RecyclerView.Adapter<ProgressModuleAdapter.ViewHolder> {

    private final Context context;
    private final List<Module> modules;
    private final AssessmentResultDAO resultDAO;
    private final UserContentViewDAO userContentViewDAO;
    private final ContentDAO contentDAO;
    private final int userId;

    public ProgressModuleAdapter(Context context, List<Module> modules) {
        this.context = context;
        this.modules = modules;
        this.resultDAO = new AssessmentResultDAO(context);
        this.userContentViewDAO = new UserContentViewDAO(context);
        this.contentDAO = new ContentDAO(context);
        this.userId = new SessionManager(context).getUserId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.progress_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Module module = modules.get(position);
        AssessmentResult result = resultDAO.getLatestResult(userId, module.getModuleID());

        int totalContent = contentDAO.getContentByModule(module.getModuleID()).size();
        int viewedContent = userContentViewDAO.getViewedCountForModule(userId, module.getModuleID());

        int progress = (totalContent > 0) ? (int) ((viewedContent / (float) totalContent) * 100) : 0;

        // Get user type & threshold
        SessionManager sessionManager = new SessionManager(context);
        String userType = sessionManager.getUserType();
        int threshold = userType.equals("volunteer") ? 100 : 80;

        // Determine quiz score percent (if any)
        int scorePercent = -1;
        if (result != null && result.getTotalQuestions() > 0) {
            scorePercent = (int) ((result.getScore() / (float) result.getTotalQuestions()) * 100);
        }

        // Determine status
        String status;
        if (progress >= 100 && scorePercent >= threshold) {
            status = "Completed";
        } else if (progress > 0 || scorePercent >= 0) {
            status = "In Progress";
        } else {
            status = "Not Started";
        }

        // Score display
        String scoreText = (scorePercent >= 0) ? "Score: " + scorePercent + "%" : "No attempts yet";

        // Badge logic (optional)
        boolean showBadge = progress == 100 && scorePercent == 100;

        // UI Bindings
        holder.tvTitle.setText(module.getModuleName());
        holder.tvStatus.setText("Status: " + status);
        holder.tvScore.setText(scoreText);
        holder.progressBar.setProgress(progress);
        holder.imgBadge.setVisibility(showBadge ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ModuleProgressDetailActivity.class);
            intent.putExtra("MODULE_ID", module.getModuleID());
            intent.putExtra("MODULE_NAME", module.getModuleName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvStatus, tvScore;
        ProgressBar progressBar;
        ImageView imgBadge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvProgressModuleTitle);
            tvStatus = itemView.findViewById(R.id.tvProgressStatus);
            tvScore = itemView.findViewById(R.id.tvProgressScore);
            progressBar = itemView.findViewById(R.id.progressTrackingBar);
            imgBadge = itemView.findViewById(R.id.imgProgressBadge);
        }
    }
}
