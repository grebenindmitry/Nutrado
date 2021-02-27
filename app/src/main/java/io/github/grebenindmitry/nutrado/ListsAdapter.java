package io.github.grebenindmitry.nutrado;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;

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

    public ListsAdapter(List<ProductList> newLists) {
        this.lists = new ArrayList<>();
        //remove the offline list
        for (ProductList list : newLists) {
            if (list.getListId() != -2) {
                this.lists.add(list);
            }
        }
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

        //if the app is offline style the online list as offline
        if (preferences.getBoolean("offline", false) && list.getListId() == -1) {
            ((TextView) view.findViewById(R.id.list_title)).setText(context.getString(R.string.offline));
            ((TextView) view.findViewById(R.id.list_desc)).setText(context.getString(R.string.offline_desc));
            ((ImageView) view.findViewById(R.id.list_icon)).setImageResource(R.drawable.ic_outline_wifi_off_24);
            materialCardView.setCardBackgroundColor(0x7FE53935);
            //if the current list selected is offline make it selected
            if (preferences.getInt("selectedList", -1) == -2) {
                materialCardView.setCardBackgroundColor(0x7F721C1A);
            }
        //if the current viewholder represents a list that is selected mark it
        } else if (preferences.getInt("selectedList", -1) == list.getListId()) {
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
        return this.lists.size();
    }
}
