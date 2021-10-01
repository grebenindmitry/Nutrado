package io.github.grebenindmitry.nutrado

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData

class AdapterList(
    private val lists: List<StructList>,
    private val selectedList: MutableLiveData<Int>,
    private val isNetworkAvailable: Boolean,
    private val activity: Activity) : RecyclerView.Adapter<AdapterList.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.nav_list_recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.view
        val materialCardView: MaterialCardView = view.findViewById(R.id.list_card)

        val listTitle = view.findViewById<TextView>(R.id.list_title)
        val listDesc = view.findViewById<TextView>(R.id.list_desc)
        val listIcon = view.findViewById<ImageView>(R.id.list_icon)

        if (isNetworkAvailable && position == 0) {
            listTitle.text = activity.getString(R.string.online)

            listDesc.text = activity.getString(R.string.online_desc)
            listIcon.setImageResource(R.drawable.ic_outline_wifi_24)
            materialCardView.setCardBackgroundColor(
                view.resources.getColor(
                    if (selectedList.value == -2) R.color.online_selected else R.color.online,
                    null)
            )
            materialCardView.setOnClickListener { selectedList.postValue(-2) }
        } else {
            val list = lists[position - if (isNetworkAvailable) 1 else 0]
            listTitle.text = list.name
            listDesc.text = list.description
            listIcon.setImageResource(list.icon)
            materialCardView.setCardBackgroundColor(
                if (selectedList.value == list.listId) list.selColor.toArgb() else list.deselColor.toArgb())
            materialCardView.setOnClickListener { selectedList.postValue(list.listId) }
        }
    }

    override fun getItemCount(): Int { return lists.size + if (isNetworkAvailable) 1 else 0 }
}