package io.github.grebenindmitry.nutrado

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.CodeScanner
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import android.widget.Toast
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.budiyev.android.codescanner.CodeScannerView
import android.widget.TextView
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.content.DialogInterface
import android.view.View
import com.budiyev.android.codescanner.DecodeCallback
import com.google.zxing.Result
import java.util.ArrayList
import java.util.concurrent.Executors
import kotlin.Exception

class ActivityAddProduct(private val TAG: String = "add-product") : AppCompatActivity() {

    private lateinit var mCodeScanner: CodeScanner
    private val requestPermissionLauncher =
        registerForActivityResult(RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(
                    this,
                    "Scanner unavailable without Camera permission",
                    Toast.LENGTH_LONG).show()
            } else {
                Utility().reloadActivity(this)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        //if got the camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED) {
            val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
            mCodeScanner = CodeScanner(this, scannerView)
            mCodeScanner.decodeCallback = DecodeCallback { result: Result ->
                runOnUiThread {
                    (findViewById<View>(R.id.input) as TextView).text = result.text
                    findViewById<View>(R.id.save_btn).performClick()
                }
            }
            scannerView.setOnClickListener { mCodeScanner.startPreview() }
            mCodeScanner.startPreview()
            //otherwise request the permission
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        //attempt to fetch and save the product
        findViewById<View>(R.id.save_btn).setOnClickListener {
            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(StringRequest(
                "https://world.openfoodfacts.org/api/v0/product/" +
                        (findViewById<View>(R.id.input) as TextView).text +
                        ".json",
                { response: String? ->
                    Executors.newSingleThreadExecutor().execute {
                        val productResponse = Gson().fromJson(response, StructProductResponse::class.java)
                        //if found the product
                        if (productResponse.status == 1) {
                            val product = productResponse.product
                            val db = DatabaseNutrado.getDatabase(this)

                            if (db != null) {
                                val productDAO = db.productDAO()
                                val listDAO = db.listDAO()
                                val listWithProductsDAO = db.listWithProductsDAO()

                                val productLists = listDAO.lists
                                val singleItems = ArrayList<CharSequence>()
                                val singleIds = ArrayList<Int>()
                                //save the list ids and names (excluding the online/offline) in suitable structures
                                if (productLists != null) {
                                    for (productList in productLists) {
                                        singleItems.add(productList.name)
                                        singleIds.add(productList.listId)
                                    }
                                    if (singleIds.isNotEmpty()) {
                                        runOnUiThread {
                                            //create a dialog with the lists
                                            MaterialAlertDialogBuilder(this)
                                                .setTitle(this.getString(R.string.select_list))
                                                .setItems(singleItems.toTypedArray())
                                                { _: DialogInterface, which: Int ->
                                                    Executors.newSingleThreadExecutor().execute {
                                                        productDAO.insert(product)
                                                        listWithProductsDAO.addProductToList(CrossrefProductList(product.productId, singleIds[which]))
                                                        finish()
                                                    }
                                                }.show()
                                        }
                                    }
                                }
                            } else {
                                runOnUiThread { Toast
                                    .makeText(this, "Database unavailable", Toast.LENGTH_SHORT)
                                    .show() }
                            }
                            //if not found the product
                        } else {
                            runOnUiThread { Toast
                                    .makeText(this, "Product not found", Toast.LENGTH_SHORT)
                                    .show() }
                            Utility().reloadActivity(this)
                        }
                    }
                }
            ) {
                runOnUiThread { Toast
                        .makeText(this, "Could not connect to the server", Toast.LENGTH_SHORT)
                        .show() }
                Utility().reloadActivity(this)
            })
        }
    }

    override fun onResume() {
        super.onResume()
        //start scanning
        try {
            mCodeScanner.startPreview()
        }
        catch (ex: Exception) { }
    }

    override fun onPause() {
        //stop scanning
        try {
            mCodeScanner.releaseResources()
        } catch (ex: Exception) { }
        super.onPause()
    }
}