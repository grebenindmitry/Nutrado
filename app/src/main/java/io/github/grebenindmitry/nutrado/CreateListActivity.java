package io.github.grebenindmitry.nutrado;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.Executors;

public class CreateListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        Toolbar toolbar = findViewById(R.id.appbar);
        toolbar.setNavigationOnClickListener((view) -> finish());
        setSupportActionBar(toolbar);

        TextView name = findViewById(R.id.list_name_text);
        TextInputLayout nameLayout = findViewById(R.id.list_name);
        //listener to reset the error message after entering something
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nameLayout.setErrorEnabled(false);
            }
        });

        findViewById(R.id.create_list_btn).setOnClickListener((v -> {
            //if name was provided
            if (!name.getText().toString().equals("")) {
                TextView desc = findViewById(R.id.list_desc_text);

                //get icon
                int iconChip = ((ChipGroup) findViewById(R.id.list_icon_grp)).getCheckedChipId();
                int icon = R.drawable.ic_outline_bookmark_24;
                {
                    if (iconChip == R.id.chip_icon_basket) {
                        icon = R.drawable.ic_outline_shopping_basket_24;
                    } else if (iconChip == R.id.chip_icon_cart) {
                        icon = R.drawable.ic_outline_local_grocery_store_24;
                    } else if (iconChip == R.id.chip_icon_smile) {
                        icon = R.drawable.ic_outline_sentiment_very_satisfied_24;
                    } else if (iconChip == R.id.chip_icon_star) {
                        icon = R.drawable.ic_outline_star_24;
                    }
                }
                //get color
                int colorChip = ((ChipGroup) findViewById(R.id.list_color_grp)).getCheckedChipId();
                Color color = Color.valueOf(getColor(R.color.red_transparent_600));
                {
                    if (colorChip == R.id.chip_color_green) {
                        color = Color.valueOf(getColor(R.color.green_transparent_500));
                    } else if (colorChip == R.id.chip_color_blue) {
                        color = Color.valueOf(getColor(R.color.blue_transparent_700));
                    } else if (colorChip == R.id.chip_color_yellow) {
                        color = Color.valueOf(getColor(R.color.yellow_transparent_600));
                    } else if (colorChip == R.id.chip_color_purple) {
                        color = Color.valueOf(getColor(R.color.purple_transparent_600));
                    }
                }

                Color finalColor = color;
                int finalIcon = icon;
                //insert the list and stop the activity
                Executors.newSingleThreadExecutor().execute(() -> {
                    final ListDAO listDAO = MyDatabase.getDatabase(this).listDAO();
                    listDAO.insert(new ProductList(name.getText().toString(), desc.getText().toString(), finalColor, finalIcon));
                    runOnUiThread(this::finish);
                });
            } else {
                nameLayout.setErrorEnabled(true);
                nameLayout.setError((getString(R.string.missing_error, "A name")));
            }
        }));
    }
}
