package io.github.grebenindmitry.nutrado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final List<Product> productData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;

        public ViewHolder(View newView) {
            super(newView);
            view = newView;
        }

        public View getView() {
            return view;
        }
    }

    public ProductAdapter(List<Product> newProductData) {
        productData = newProductData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view = holder.getView();
        Context context = view.getContext();
        Product product = this.productData.get(position);
        if (product.getEnergy() != -2) {
            ((TextView) view.findViewById(R.id.product_name)).setText(product.getName());
            ((TextView) view.findViewById(R.id.product_energy)).setText(context
                    .getString(R.string.energy_kj, product.getPrintEnergy()));
            ((TextView) view.findViewById(R.id.product_grade)).setText(product.getGrade().toUpperCase());
            Glide.with(view)
                    .load(product.getThumbUrl())
                    .circleCrop()
                    .into(((ImageView) view.findViewById(R.id.product_image)));
            ((ImageButton) view.findViewById(R.id.button_save)).setOnClickListener(
                    (v) -> Executors.newSingleThreadExecutor().execute(
                            () -> {
                                final MyDAO dao = MyDatabase.getDatabase(v.getContext()).myDAO();
                                dao.insertList(new ProductList(position, "oof", "fuck it"));
                            })
            );
        } else {
            ((TextView) view.findViewById(R.id.product_name)).setText(product.getName());
            view.findViewById(R.id.button_save).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }
}
