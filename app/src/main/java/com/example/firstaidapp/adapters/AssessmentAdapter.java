package com.example.firstaidapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.QuizActivity;
import com.example.firstaidapp.R;
import com.example.firstaidapp.database.AssessmentResultDAO;
import com.example.firstaidapp.database.QuestionDAO;
import com.example.firstaidapp.models.AssessmentResult;
import com.example.firstaidapp.models.Module;
import com.example.firstaidapp.models.Question;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    private final Context context;
    private final List<Module> moduleList;
    private final QuestionDAO questionDAO;
    private final AssessmentResultDAO resultDAO;
    private final String userType;
    private final int userId;

    public AssessmentAdapter(Context context, List<Module> moduleList,
                             QuestionDAO questionDAO,
                             AssessmentResultDAO resultDAO,
                             String userType, int userId) {
        this.context = context;
        this.moduleList = moduleList;
        this.questionDAO = questionDAO;
        this.resultDAO = resultDAO;
        this.userType = userType;
        this.userId = userId;
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_assessment, parent, false);
        return new AssessmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        Module module = moduleList.get(position);
        List<Question> questions = questionDAO.getQuestionsByModuleId(module.getModuleID());

        int mcqCount = 0, scenarioCount = 0;
        for (Question q : questions) {
            if ("mcq".equalsIgnoreCase(q.getQuestionType())) mcqCount++;
            else if ("scenario".equalsIgnoreCase(q.getQuestionType())) scenarioCount++;
        }

        int total = mcqCount + scenarioCount;
        int required = userType.equalsIgnoreCase("volunteer") ? 100 : 80;

        holder.tvModuleTitle.setText(module.getModuleName());
        holder.tvQuestionCount.setText(total + " Questions");
        holder.tvBreakdown.setText(mcqCount + " MCQ, " + scenarioCount + " Scenario");
        holder.tvRequirement.setText("Pass requirement: " + required + "% (" + userType + ")");

        int percent = module.getProgressPercentage();
        holder.tvProgressLabel.setText("Progress: " + percent + "%");
        holder.progressScore.setProgress(percent);

        AssessmentResult result = resultDAO.getResult(userId, module.getModuleID());
        if (result != null) {
            holder.tvLastTaken.setText("🕓 Last taken: " + result.getDateTaken());
            holder.tvRetakeCount.setText("🔁 Retakes: " + result.getRetakeCount());
        } else {
            holder.tvLastTaken.setText("🕓 Last taken: —");
            holder.tvRetakeCount.setText("🔁 Retakes: 0");
        }

        boolean isCompleted = "Completed".equalsIgnoreCase(module.getCompletionStatus());
        holder.tvAssessmentBadge.setVisibility(isCompleted ? View.VISIBLE : View.GONE);
        holder.imgLock.setVisibility(isCompleted ? View.GONE : View.VISIBLE);

        if (!isCompleted) {
            holder.btnStart.setAlpha(0.5f);
            holder.btnStart.setText("Locked");
            holder.btnStart.setOnClickListener(v -> {
                Toast.makeText(context, "Please complete the module first.", Toast.LENGTH_SHORT).show();
            });
        } else {
            holder.btnStart.setAlpha(1f);
            holder.btnStart.setText("Start Quiz");
            holder.btnStart.setOnClickListener(v -> {
                // Log quiz_started to Firebase
                Bundle bundle = new Bundle();
                bundle.putString("module_name", module.getModuleName());
                bundle.putInt("module_id", module.getModuleID());
                FirebaseAnalytics.getInstance(context).logEvent("quiz_started", bundle);

                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra("MODULE_ID", module.getModuleID());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    static class AssessmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvModuleTitle, tvQuestionCount, tvBreakdown, tvRequirement;
        TextView tvProgressLabel, tvLastTaken, tvRetakeCount, tvAssessmentBadge;
        ImageView imgLock;
        ProgressBar progressScore;
        Button btnStart;

        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModuleTitle = itemView.findViewById(R.id.tvAssessmentTitle);
            tvQuestionCount = itemView.findViewById(R.id.tvQuestionCount);
            tvBreakdown = itemView.findViewById(R.id.tvTypeBreakdown);
            tvRequirement = itemView.findViewById(R.id.tvPassRequirement);
            tvProgressLabel = itemView.findViewById(R.id.tvProgressLabel);
            tvLastTaken = itemView.findViewById(R.id.tvLastTaken);
            tvRetakeCount = itemView.findViewById(R.id.tvRetakeCount);
            tvAssessmentBadge = itemView.findViewById(R.id.tvAssessmentBadge);
            progressScore = itemView.findViewById(R.id.progressScore);
            btnStart = itemView.findViewById(R.id.btnStartAssessment);
            imgLock = itemView.findViewById(R.id.imgLock);
        }
    }
}
