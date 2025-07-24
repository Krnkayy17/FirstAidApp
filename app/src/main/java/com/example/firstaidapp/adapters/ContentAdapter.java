package com.example.firstaidapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.R;
import com.example.firstaidapp.SubTopicDetailActivity;
import com.example.firstaidapp.models.Content;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {

    private final Context context;
    private final List<Content> sectionList; // One entry per contentOrder group

    public ContentAdapter(Context context, List<Content> allContent) {
        this.context = context;
        this.sectionList = new ArrayList<>();

        // HashSet to keep track of already added content orders
        Set<Integer> seenOrders = new HashSet<>();

        for (Content content : allContent) {
            int order = content.getContentOrder();
            String title = content.getContentTitle();

            // Only add the first content with each unique order and a non-empty title
            if (!seenOrders.contains(order) && title != null && !title.trim().isEmpty()) {
                sectionList.add(content);
                seenOrders.add(order);
            }
        }
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subtopic_card, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        Content content = sectionList.get(position);
        holder.tvContentTitle.setText(content.getContentTitle());

        // Navigate to detailed content when clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SubTopicDetailActivity.class);
            intent.putExtra("MODULE_ID", content.getModuleId());
            intent.putExtra("CONTENT_ORDER", content.getContentOrder());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView tvContentTitle;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContentTitle = itemView.findViewById(R.id.tvContentTitle);
        }
    }
}
