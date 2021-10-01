package io.github.grebenindmitry.nutrado

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import java.util.concurrent.Executors

class AdapterProduct(private val productData: List<StructProduct>, private val activity: Activity) :
    RecyclerView.Adapter<AdapterProduct.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.view
        val product = productData[position]
        (view.findViewById<View>(R.id.product_name) as TextView).text = product.name
        (view.findViewById<View>(R.id.product_energy) as TextView).text = activity
            .getString(R.string.energy_kj, product.printEnergy)
        (view.findViewById<View>(R.id.product_grade) as TextView).text = product.grade.uppercase()
        Glide
            .with(view)
            .load(product.thumbUrl)
            .circleCrop()
            .into((view.findViewById<View>(R.id.product_image) as ImageView))
        view.setOnClickListener { v: View ->
            Executors.newSingleThreadExecutor().execute {

                //save the product so we can access it in the info activity
                val productDAO = DatabaseNutrado.getDatabase(v.context)?.productDAO()
                if (productDAO != null) {
                    productDAO.insert(product)
                    activity.startActivity(Intent(activity, ActivityProduct::class.java)
                        .putExtra("id", product.productId))
                } else {
                    activity.runOnUiThread { Toast
                        .makeText(activity, "Database not available", Toast.LENGTH_SHORT)
                        .show() }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return productData.size
    }
}