package io.github.grebenindmitry.nutrado;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder> {
    private final List<ProductList> lists;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public View getView() {
            return this.view;
        }
    }

    public ListsAdapter(List<ProductList> lists) {
        this.lists = lists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nav_list_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view = holder.getView();
        Context context = view.getContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        MaterialCardView materialCardView = view.findViewById(R.id.list_card);
        ProductList list = this.lists.get(position);
        ((TextView) view.findViewById(R.id.list_title)).setText(list.getName());
        ((TextView) view.findViewById(R.id.list_desc)).setText(list.getDescription());
        ((ImageView) view.findViewById(R.id.list_icon)).setImageResource(list.getIcon());
        if (preferences.getInt("selectedList", -1) == list.getListId()) {
            materialCardView.setCardBackgroundColor(list.getDarkColor().toArgb());
        } else {
            materialCardView.setCardBackgroundColor(list.getColor().toArgb());
        }
        materialCardView.setOnClickListener((view1) -> {
            preferences.edit().putInt("selectedList", list.getListId()).apply();
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
