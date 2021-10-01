package io.github.grebenindmitry.nutrado

import android.app.Activity

class Utility {
    fun reloadActivity(activity: Activity) {
        activity.finish()
        activity.overridePendingTransition(0, 0)
        activity.startActivity(activity.intent)
        activity.overridePendingTransition(0, 0)
    }

    fun reloadActivity(activity: Activity, selectedList: Int) {
        activity.finish()
        activity.overridePendingTransition(0, 0)
        activity.startActivity(activity.intent.putExtra("selectedList", selectedList))
        activity.overridePendingTransition(0, 0)
    }
}