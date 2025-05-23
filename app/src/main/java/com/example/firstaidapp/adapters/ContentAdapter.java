package com.example.firstaidapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Make sure to add Glide to your dependencies
import com.example.firstaidapp.R;
import com.example.firstaidapp.SubTopicDetailActivity;
import com.example.firstaidapp.models.Content;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {

    private List<Content> contentList;
    private Context context;

    public ContentAdapter(Context context, List<Content> contentList) {
        this.context = context;
        this.contentList = contentList;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subtopic_card, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        Content content = contentList.get(position);
        holder.tvContentTitle.setText(content.getContentTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SubTopicDetailActivity.class);
            intent.putExtra("CONTENT_TITLE", content.getContentTitle());
            intent.putExtra("CONTENT_TEXT", content.getContentText());
            intent.putExtra("CONTENT_IMAGE", content.getContentImage());
            intent.putExtra("CONTENT_URL", content.getContentURL());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView tvContentTitle;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContentTitle = itemView.findViewById(R.id.tvContentTitle);
        }
    }
}
