package com.example.firstaidapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firstaidapp.adapters.ScenarioStepAdapter;
import com.example.firstaidapp.database.QuestionDAO;
import com.example.firstaidapp.models.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScenarioStepAdapter extends RecyclerView.Adapter<ScenarioStepAdapter.StepViewHolder> {

    private RecyclerView recyclerSteps;
    private ScenarioStepAdapter adapter;
    private Button btnSubmit;

    private Question currentQuestion;
    private List<String> correctSequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_scenario);

        recyclerSteps = findViewById(R.id.recycler_steps);
        btnSubmit = findViewById(R.id.btn_submit_scenario);

        int moduleId = getIntent().getIntExtra("MODULE_ID", 1);
        QuestionDAO dao = new QuestionDAO(this);

        // Load first scenario question only
        for (Question q : dao.getQuestionsByModuleId(moduleId)) {
            if ("scenario".equalsIgnoreCase(q.getQuestionType())) {
                currentQuestion = q;
                break;
            }
        }

        if (currentQuestion == null) {
            Toast.makeText(this, "No scenario questions found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        correctSequence = Arrays.asList(currentQuestion.getCorrectSequence().split(","));
        List<String> shuffledSteps = new ArrayList<>(correctSequence);
        Collections.shuffle(shuffledSteps);

        adapter = new ScenarioStepAdapter(shuffledSteps);
        recyclerSteps.setAdapter(adapter);
        recyclerSteps.setLayoutManager(new LinearLayoutManager(this));

        // Enable drag-and-drop
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder from, @NonNull RecyclerView.ViewHolder to) {
                adapter.onItemMove(from.getAdapterPosition(), to.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Do nothing
            }
        });
        helper.attachToRecyclerView(recyclerSteps);

        btnSubmit.setOnClickListener(v -> checkAnswer());

        ImageView imgScenario = findViewById(R.id.img_scenario);
        Button btnReset = findViewById(R.id.btn_reset_order);

        // Load scenario image
        if (currentQuestion.getQuestionImage() != null && !currentQuestion.getQuestionImage().isEmpty()) {
            imgScenario.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(getImageResourceId(currentQuestion.getQuestionImage()))
                    .placeholder(R.drawable.image_placeholder)
                    .into(imgScenario);
        } else {
            imgScenario.setVisibility(View.GONE);
        }

        // Reset button
        btnReset.setOnClickListener(v -> {
            Collections.shuffle(correctSequence); // shuffle the steps again
            adapter = new ScenarioStepAdapter(new ArrayList<>(correctSequence));
            recyclerSteps.setAdapter(adapter);
        });

    }

    private int getImageResourceId(String imageName) {
        return getResources().getIdentifier(imageName.replace(".png", ""), "drawable", getPackageName());
    }

    private void checkAnswer() {
        List<String> userOrder = adapter.getCurrentOrder();
        if (userOrder.equals(correctSequence)) {
            Toast.makeText(this, "✅ Correct sequence!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "❌ Incorrect order.\nCorrect: " + String.join(" ➜ ", correctSequence), Toast.LENGTH_LONG).show();
        }
    }
}
