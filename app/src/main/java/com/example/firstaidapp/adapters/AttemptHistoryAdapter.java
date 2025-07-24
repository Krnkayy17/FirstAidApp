package com.example.firstaidapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.R;
import com.example.firstaidapp.models.AssessmentResult;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AttemptHistoryAdapter extends RecyclerView.Adapter<AttemptHistoryAdapter.ViewHolder> {

    private final List<AssessmentResult> attempts;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
    private final Context context;
    private final String userType;

    // Constructor
    public AttemptHistoryAdapter(Context context, List<AssessmentResult> attempts, String userType) {
        this.context = context;
        this.attempts = attempts;
        this.userType = userType;
    }

    @NonNull
    @Override
    public AttemptHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attempt_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttemptHistoryAdapter.ViewHolder holder, int position) {
        AssessmentResult attempt = attempts.get(position);

        // Format date or show "N/A"
        String dateText = attempt.getDateTaken() != null
                ? dateFormat.format(attempt.getDateTaken())
                : "N/A";

        // Calculate score percentage
        int score = attempt.getScore();
        int total = attempt.getTotalQuestions();
        float percentage = total > 0 ? ((float) score / total) * 100f : 0;

        // Determine if user pass the threshold based on user type
        float passThreshold = userType.equalsIgnoreCase("VAD") ? 100f : 80f;
        boolean isPassed = percentage >= passThreshold;

        // Display attempt details
        int attemptNumber = getItemCount() - position;
        holder.tvDate.setText("Attempt " + attemptNumber + " • " + dateText);
        holder.tvScore.setText("Score: " + score + "/" + total + " (" + Math.round(percentage) + "%)");
        holder.tvRetake.setText("Retake Count: " + attempt.getRetakeCount());

        holder.tvStatus.setText(isPassed ? "Status: Passed ✅" : "Status: Failed ❌");
        holder.tvStatus.setTextColor(ContextCompat.getColor(context,
                isPassed ? android.R.color.holo_green_dark : android.R.color.holo_red_dark));
        holder.tvLatest.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    // Returns the number of attempt items in the list
    public int getItemCount() {
        return attempts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvScore, tvRetake, tvStatus, tvLatest;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvAttemptDate);
            tvScore = itemView.findViewById(R.id.tvAttemptScore);
            tvRetake = itemView.findViewById(R.id.tvAttemptRetake);
            tvStatus = itemView.findViewById(R.id.tvAttemptStatus);
            tvLatest = itemView.findViewById(R.id.tvLatestAttempt);
        }
    }
}
