package com.orion.videourlcast;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {
    private final MediaCallback callback;
    VideoModel model;
    ArrayList<VideoMetaData> videoMetaData;
    Context context;

    public VideosAdapter(Activity context, VideoModel model, MediaCallback callback) {
        this.context = context;
        this.model = model;
        this.callback = callback;
        this.videoMetaData = model.getVideosMetaData();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.des.setText(videoMetaData.get(position).getTitle() + " \n" + videoMetaData.get(position).getSources().get(0));
    }

    @Override
    public int getItemCount() {
        return videoMetaData != null ? videoMetaData.size() : 0;
    }

    public void setData(ArrayList<VideoMetaData> videoMetaData) {
        this.videoMetaData = videoMetaData;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView des;

        public ViewHolder(View itemView) {
            super(itemView);
            des = (TextView) itemView.findViewById(R.id.description);
            des.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    VideoMetaData metaData = videoMetaData.get(pos);
                    callback.onVideoSelected(metaData);
                }
            });
        }
    }

}
