package com.example.firstaidapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firstaidapp.R;
import com.example.firstaidapp.models.VideoRecommendation;

import java.util.List;

public class VideoRecommendationAdapter extends RecyclerView.Adapter<VideoRecommendationAdapter.VideoViewHolder> {

    private List<VideoRecommendation> videoList;
    private Context context;
    private OnVideoClickListener listener;

    // Custom listener constructor
    public VideoRecommendationAdapter(List<VideoRecommendation> videoList, Context context, OnVideoClickListener listener) {
        this.videoList = videoList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommendation, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoRecommendation video = videoList.get(position);
        holder.tvTitle.setText(video.getTitle());

        // Use pre-generated thumbnail URL
        String thumbnailUrl = video.getThumbnailUrl();
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            Glide.with(context)
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.video_placeholder)
                    .into(holder.imgThumbnail);
        } else {
            holder.imgThumbnail.setImageResource(R.drawable.video_placeholder);
        }

        // Open video on click
        View.OnClickListener openVideo = v -> {
            if (listener != null) {
                listener.onVideoClick(video);
            }
        };

        holder.btnWatch.setOnClickListener(openVideo);
        holder.itemView.setOnClickListener(openVideo);
    }


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public interface OnVideoClickListener {
        void onVideoClick(VideoRecommendation video);
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView tvTitle;
        Button btnWatch;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.img_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title);
            btnWatch = itemView.findViewById(R.id.btn_watch);
        }
    }
}
