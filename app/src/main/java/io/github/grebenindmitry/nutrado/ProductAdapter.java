package io.github.grebenindmitry.nutrado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final ProductData[] productData;

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

    public ProductAdapter(ProductData[] newProductData) {
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
        ((TextView) view.findViewById(R.id.product_name)).setText(productData[position].getName());
        ((TextView) view.findViewById(R.id.product_energy)).setText(context
                .getString(R.string.energy_kj, productData[position].getPrintEnergy()));
        String oof = productData[position].getGrade().toUpperCase();
        ((TextView) view.findViewById(R.id.product_grade)).setText(oof);
        Glide.with(view)
                .load(productData[position].getThumbUrl())
                .circleCrop()
                .into(((ImageView) view.findViewById(R.id.product_image)));
    }

    @Override
    public int getItemCount() {
        return productData.length;
    }
}
