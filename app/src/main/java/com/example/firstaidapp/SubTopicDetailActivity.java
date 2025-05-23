package com.example.firstaidapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firstaidapp.database.ContentDAO;
import com.example.firstaidapp.database.FirstAidDatabaseHelper;
import com.example.firstaidapp.models.Content;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SubTopicDetailActivity extends AppCompatActivity {

    private TextView detailTitle, detailText, detailUrl;
    private ImageView detailImage;
    private Button nextButton;

    private int moduleId;
    private int contentOrder;
    private ContentDAO contentDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_topic_detail);

        detailTitle = findViewById(R.id.detailTitle);
        detailText = findViewById(R.id.detailText);
        detailImage = findViewById(R.id.detailImage);
        detailUrl = findViewById(R.id.detailUrl);
        nextButton = findViewById(R.id.nextButton);

        contentDAO = new ContentDAO(new FirstAidDatabaseHelper(this));

        // Get data from Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("CONTENT_TITLE");
        String text = intent.getStringExtra("CONTENT_TEXT");
        String imageUrl = intent.getStringExtra("CONTENT_IMAGE");
        String url = intent.getStringExtra("CONTENT_URL");
        moduleId = intent.getIntExtra("MODULE_ID", -1);
        contentOrder = intent.getIntExtra("CONTENT_ORDER", -1);

        detailTitle.setText(title);
        detailText.setText(text);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            @SuppressLint("DiscouragedApi") int imageResId = getResources().getIdentifier(imageUrl, "drawable", getPackageName());
            if (imageResId != 0) {
                Glide.with(this)
                        .load(imageResId)
                        .override(1024, 1024)
                        .centerInside()
                        .into(detailImage);
                detailImage.setVisibility(View.VISIBLE);
            } else {
                detailImage.setVisibility(View.GONE); // or load a placeholder image
            }
        } else {
            detailImage.setVisibility(View.GONE);
        }


        if (url != null && !url.isEmpty()) {
            detailUrl.setText("More Info: " + url);
            detailUrl.setVisibility(View.VISIBLE);
        } else {
            detailUrl.setVisibility(View.GONE);
        }

    }
}
