package io.github.grebenindmitry.nutrado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Vector;

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
        ProductList list = this.lists.get(position);
        if (!lists.isEmpty()) {
            ((TextView) view.findViewById(R.id.list_title)).setText(list.getName());
            ((TextView) view.findViewById(R.id.list_desc)).setText(list.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
