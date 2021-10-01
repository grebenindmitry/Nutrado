package io.github.grebenindmitry.nutrado

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.chip.ChipGroup
import java.util.concurrent.Executors

class ActivityCreateList(private val TAG: String = "create-list") : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_list)

        val toolbar = findViewById<Toolbar>(R.id.appbar)
        val name = findViewById<TextView>(R.id.list_name_text)
        val nameLayout = findViewById<TextInputLayout>(R.id.list_name)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        //listener to reset the error message after entering something
        name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) { nameLayout.isErrorEnabled = false }
        })
        findViewById<View>(R.id.create_list_btn).setOnClickListener {
            //if name was provided
            if (name.text.toString() != "") {
                val desc = findViewById<TextView>(R.id.list_desc_text)

                //get icon
                val iconChip = (findViewById<View>(R.id.list_icon_grp) as ChipGroup).checkedChipId
                var icon = R.drawable.ic_outline_bookmark_24

                when (iconChip) {
                    R.id.chip_icon_basket -> { icon = R.drawable.ic_outline_shopping_basket_24 }
                    R.id.chip_icon_cart -> { icon = R.drawable.ic_outline_local_grocery_store_24 }
                    R.id.chip_icon_smile -> { icon = R.drawable.ic_outline_sentiment_very_satisfied_24 }
                    R.id.chip_icon_star -> { icon = R.drawable.ic_outline_star_24 }
                }

                //get color
                val colorChip = (findViewById<View>(R.id.list_color_grp) as ChipGroup).checkedChipId
                var color = Color.valueOf(getColor(R.color.red_transparent_600))

                when (colorChip) {
                    R.id.chip_color_green -> { color = Color.valueOf(getColor(R.color.green_transparent_500)) }
                    R.id.chip_color_blue -> { color = Color.valueOf(getColor(R.color.blue_transparent_700)) }
                    R.id.chip_color_yellow -> { color = Color.valueOf(getColor(R.color.yellow_transparent_600)) }
                    R.id.chip_color_purple -> { color = Color.valueOf(getColor(R.color.purple_transparent_600)) }
                }

                val finalColor = color
                val finalIcon = icon
                //insert the list and stop the activity
                Executors.newSingleThreadExecutor().execute {
                    val listDAO = DatabaseNutrado.getDatabase(this)?.listDAO()
                    if (listDAO != null) {
                        listDAO.insert(StructList(
                                name.text.toString(),
                                desc.text.toString(),
                                finalColor,
                                finalIcon))
                        runOnUiThread { finish() }
                    } else {
                        runOnUiThread { Toast
                                    .makeText(this, "Database not available", Toast.LENGTH_SHORT)
                                    .show() }
                    }
                }
            } else {
                nameLayout.isErrorEnabled = true
                nameLayout.error = getString(R.string.missing_error, "A name")
            }
        }
    }
}