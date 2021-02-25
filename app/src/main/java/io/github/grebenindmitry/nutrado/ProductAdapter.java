package io.github.grebenindmitry.nutrado;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

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
        ((TextView) view.findViewById(R.id.product_name)).setText(product.getName());
        ((TextView) view.findViewById(R.id.product_energy)).setText(context
                .getString(R.string.energy_kj, product.getPrintEnergy()));
        ((TextView) view.findViewById(R.id.product_grade)).setText(product.getGrade().toUpperCase());
        Glide.with(view)
                .load(product.getThumbUrl())
                .circleCrop()
                .into(((ImageView) view.findViewById(R.id.product_image)));
        view.setOnClickListener(
                (v) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        final ProductDAO productDAO = MyDatabase.getDatabase(v.getContext()).productDAO();
                        productDAO.insert(product);
                        Intent intent = new Intent(context, ProductActivity.class);
                        intent.putExtra("id", product.getProductId());
                        context.startActivity(intent);
                    });
                }
        );
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }
}
