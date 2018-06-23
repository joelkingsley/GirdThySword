package com.code.codemercenaries.girdthysword.ListAdapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.code.codemercenaries.girdthysword.Objects.LeaderboardUser;
import com.code.codemercenaries.girdthysword.R;

import java.util.List;

/**
 * Created by Joel Kingsley on 04-06-2018.
 */

public class LCustomAdapter extends RecyclerView.Adapter<LCustomAdapter.ViewHolder> {

    Activity activity;
    List<LeaderboardUser> users;


    public LCustomAdapter(Activity activity, List<LeaderboardUser> users) {
        this.activity = activity;
        this.users = users;
    }

    @NonNull
    @Override
    public LCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_custom_list, parent, false);
        ViewHolder vh = new ViewHolder(rootView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LCustomAdapter.ViewHolder holder, int position) {
        Glide.with(activity).load(users.get(position).getProfileURL()).into(holder.profile);
        holder.rank.setText(Integer.toString(users.get(position).getRank()));
        holder.name.setText(users.get(position).getDisplayName());
        holder.level.setText(activity.getResources().getString(R.string.default_level, users.get(position).getLevel(), users.get(position).getStatus()));
        holder.versesMemorized.setText(users.get(position).getVersesMemorized().toString());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile;
        public TextView rank;
        public TextView name;
        public TextView level;
        public TextView versesMemorized;

        public ViewHolder(View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile);
            rank = itemView.findViewById(R.id.rank);
            name = itemView.findViewById(R.id.displayName);
            level = itemView.findViewById(R.id.level);
            versesMemorized = itemView.findViewById(R.id.verses_memorized);
        }
    }
}
