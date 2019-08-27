package com.bibinetalkole.app.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bibinetalkole.app.R;
import com.bibinetalkole.app.models.Shows;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ShowArrayAdapter extends  RecyclerView.Adapter<ShowArrayAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<Shows> mListShows;
    Context context;
    // Pass in the contact array into the constructor
    public ShowArrayAdapter(List<Shows> shows) {
        mListShows = shows;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_show, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(context, contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int iPosition) {
        Shows show = mListShows.get(iPosition);

        // Set item views based on your views and data model
        TextView title = viewHolder.tvTitle;
        title.setText(Html.fromHtml(show.getPostTitle()));


        ImageView imgPost = viewHolder.ivImage;
        Glide.with(context)
                .load(show.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerInside()
                .placeholder(R.drawable.bibi_logo_show)
                .error(R.drawable.bibi_logo_show)
                .apply(new RequestOptions().override(300, 300))
                .into(imgPost);
    }

    @Override
    public int getItemCount() {
        return mListShows.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvTitle;
        public ImageView ivImage;
        private Context contexts;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.tvArticleTitle);
            this.ivImage = itemView.findViewById(R.id.ivArticleImage);
            // Store the context
            this.contexts = context;
            // Attach a click listener to the entire row view
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ViewHolder holder = (ViewHolder) v.getTag();
            int position = getAdapterPosition(); // gets item position

        }
    }

}
