package io.github.grebenindmitry.nutrado

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.concurrent.Executors
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityMain(
    private val TAG: String = "main",
    private val productsLive: MutableLiveData<List<StructProduct>> = MutableLiveData(listOf()),
    private val listsLive: MutableLiveData<List<StructList>> = MutableLiveData(listOf())
) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DatabaseNutrado.getDatabase(this)

        val toolbar = findViewById<Toolbar>(R.id.bottomAppBar)
        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val addListBtn = findViewById<ExtendedFloatingActionButton>(R.id.add_list_btn)
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        val navRecyclerView = findViewById<RecyclerView>(R.id.nav_list_recycler)
        val mainRecyclerView = findViewById<RecyclerView>(R.id.main_recycler_view)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val selectedList: MutableLiveData<Int> = MutableLiveData<Int>(-1)

        navRecyclerView.layoutManager = LinearLayoutManager(this)
        mainRecyclerView.layoutManager = LinearLayoutManager(this)

        class NavObserver(private val isNetworkAvailable: Boolean) : Observer<List<StructList>> {
            override fun onChanged(value: List<StructList>) {
                runOnUiThread { navRecyclerView.adapter = AdapterList(value, selectedList, isNetworkAvailable,  this@ActivityMain) }
            }
        }
        class OOFNetworkCallback : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                runOnUiThread { listsLive.observe(this@ActivityMain, NavObserver(true)) }
                if (selectedList.value == -1) selectedList.postValue(-2)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                runOnUiThread { listsLive.observe(this@ActivityMain, NavObserver(false)) }
            }
        }
        class SelListObserver : Observer<Int> {
            override fun onChanged(value: Int) {
                navRecyclerView.adapter?.notifyDataSetChanged()
                showList(value)
                drawerLayout.close()
            }
        }

        selectedList.observe(this, SelListObserver())

        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(), OOFNetworkCallback()
        )

        class ProductObserver : Observer<List<StructProduct>> {
            override fun onChanged(list: List<StructProduct>) {
                mainRecyclerView.adapter = AdapterProduct(list, this@ActivityMain)
                for (item in list) {
                    Log.d(TAG, item.name)
                }
            }
        }

        productsLive.observe(this, ProductObserver())

        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            startActivity(Intent(this, ActivityAddProduct::class.java))
        }

        toolbar.setNavigationOnClickListener { findViewById<DrawerLayout>(R.id.drawer_layout).open() }

        addListBtn.setOnClickListener {
            startActivity(Intent(this, ActivityCreateList::class.java))
        }

        swipeRefreshLayout.setOnRefreshListener { Utility().reloadActivity(this) }
    }

    fun showList(selectedList: Int) {
        Executors.newSingleThreadExecutor().execute {
            val dao = DatabaseNutrado.getDatabase(this)?.listWithProductsDAO()

            when (selectedList) {
                -2 -> { productsLive.postValue(listOf()) }
                else -> {
                    if (dao != null) {
                        dao.getListWithProducts(selectedList)?.let { listWithProducts ->
                            listWithProducts.products?.let {
                                productsLive.postValue(it)
                            }
                        }
                    } else {
                        runOnUiThread { Toast
                            .makeText(this@ActivityMain, "Database not available", Toast.LENGTH_SHORT)
                            .show() }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        Executors.newSingleThreadExecutor().execute {
            val dao = DatabaseNutrado.getDatabase(this)?.listDAO()
            if (dao != null) {
                val listOfLists = dao.lists
                listsLive.postValue(listOfLists)
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        if (this.preferences.getInt("selectedList", -1) === -1) {
//            menu.findItem(R.id.action_delete_list).isVisible = false
//        }
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == R.id.action_settings) {
//            startActivity(Intent(this, SettingsActivity::class.java))
//            return true
//        } else if (id == R.id.action_delete_list) {
//            //create a dialog to confirm deletion of list
//            MaterialAlertDialogBuilder(this)
//                .setTitle(R.string.delete_list)
//                .setMessage(R.string.delete_list_msg)
//                .setPositiveButton(R.string.yes,
//                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
//                        Executors.newSingleThreadExecutor().execute {
//                            val listDAO = MyDatabase.getDatabase(this).listDAO()
//                            listDAO.delete(listDAO.getList(selectedList))
//                            //change the selected list away from the deleted one and reload
//                            runOnUiThread {
//                                preferences.edit().putInt("selectedList", -1).commit()
//                                reloadActivity()
//                            }
//                        }
//                    })
//                .setNeutralButton(R.string.cancel,
//                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int -> })
//                .show()
//        }
//        return super.onOptionsItemSelected(item)
//    }
}