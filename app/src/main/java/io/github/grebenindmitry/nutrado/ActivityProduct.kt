package io.github.grebenindmitry.nutrado

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.bumptech.glide.Glide
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.content.DialogInterface
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import java.util.ArrayList
import java.util.concurrent.Executors

class ActivityProduct(private val TAG: String = "prod-activity") : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_info)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //make the back button kill the activity
        toolbar.setNavigationOnClickListener { finish() }

        //get the product id passed from the main activity
        val prodId = intent.getStringExtra("id") ?: "null"
        Executors.newSingleThreadExecutor().execute {
            val db = DatabaseNutrado.getDatabase(this)
            if (db != null) {
                val productDAO = db.productDAO()
                val productInListsDAO = db.productInListsDAO()

                //if the item is in some list, make it deletable
                var deletable: Boolean
                productInListsDAO.getProductInLists(prodId).let { deletable = true }
                val product = productDAO.getProduct(prodId)

                if (product != null) {
                    runOnUiThread {
                        val saveButton = findViewById<MaterialButton>(R.id.save_btn)
                        val prodImage = findViewById<ImageView>(R.id.product_image)
                        val deleteButton = findViewById<MaterialButton>(R.id.delete_btn)

                        toolbar.title = product.name
                        Glide.with(this)
                            .load(product.imageUrl)
                            .circleCrop()
                            .into(prodImage)
                        (findViewById<View>(R.id.nutrition_grade) as TextView).text = product.grade
                        (findViewById<View>(R.id.ingredients) as TextView).text = product.ingredients
                        deleteButton.isEnabled = deletable
                        deleteButton.setOnClickListener {
                            Executors.newSingleThreadExecutor().execute {
                                //delete the product and kill activity
                                productDAO.delete(product)
                                finish()
                            }
                        }
                        saveButton.isEnabled = true
                        saveButton.setOnClickListener {
                            Executors.newSingleThreadExecutor().execute {
                                val listDAO = db.listDAO()
                                val listWithProductsDAO = db.listWithProductsDAO()
                                val productLists = listDAO.lists
                                val singleItems = ArrayList<String>()
                                val singleIds = ArrayList<Int>()

                                //save the list ids and names (excluding the online/offline) in suitable structures
                                if (productLists != null) {
                                    for (productList in productLists) {
                                        if (productList.listId > 0) {
                                            singleItems.add(productList.name)
                                            singleIds.add(productList.listId)
                                        }
                                    }
                                }
                                runOnUiThread {
                                    //create a dialog with the lists
                                    MaterialAlertDialogBuilder(this)
                                        .setTitle(this.getString(R.string.select_list))
                                        .setItems(singleItems.toTypedArray()) { _: DialogInterface?, which: Int ->
                                            Executors.newSingleThreadExecutor().execute {
                                                //add to list and reload to show the delete button
                                                listWithProductsDAO.addProductToList(
                                                    CrossrefProductList(product.productId, singleIds[which]))
                                                Utility().reloadActivity(this)
                                            }
                                        }.show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}