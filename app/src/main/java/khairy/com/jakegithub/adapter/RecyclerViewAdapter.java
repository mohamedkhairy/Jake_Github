package khairy.com.jakegithub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import khairy.com.jakegithub.R;
import khairy.com.jakegithub.database.entity.GithubModel;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private List<GithubModel> gitList;
    private RecyclerView.ViewHolder viewHolder;
    private Boolean isEnded = false;

    public RecyclerViewAdapter(List<GithubModel> gitList) {
        this.gitList = gitList;
    }

    public void addItems(List<GithubModel> newItems, Boolean isEnded) {
        gitList = newItems;
        this.isEnded = isEnded;
        notifyDataSetChanged();

    }


    @Override
    public int getItemViewType(int position) {
        if (position == gitList.size() - 1 && gitList.size() < 108) {
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_NORMAL;

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                viewHolder = new CardViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
                return viewHolder;


            case VIEW_TYPE_LOADING:
                viewHolder = new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
                return viewHolder;
            default:
                return null;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CardViewHolder) {

            ((CardViewHolder) holder).name.setText(gitList.get(position).getName());
            ((CardViewHolder) holder).description.setText(gitList.get(position).getDescription());
            ((CardViewHolder) holder).linkUrl.setText(gitList.get(position).getHtmlUrl());
        } else {

            if (!isEnded) {

                ((ProgressHolder) holder).linearLayoutLoading.setVisibility(View.VISIBLE);
            } else {

                ((ProgressHolder) holder).linearLayoutLoading.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return gitList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView description;
        private TextView linkUrl;
        private CardView cardView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            this.name = (TextView) itemView.findViewById(R.id.name);
            this.description = (TextView) itemView.findViewById(R.id.description);
            this.linkUrl = (TextView) itemView.findViewById(R.id.link_url);
            this.cardView = (CardView) itemView.findViewById(R.id.main_card);
        }
    }

    public class ProgressHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayoutLoading;

        public ProgressHolder(@NonNull View itemView) {
            super(itemView);
            this.linearLayoutLoading = (LinearLayout) itemView.findViewById(R.id.progress);
        }
    }

}
