package com.example.easycoin;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class SettingFragment extends AppCompatActivity {
    private Switch switchMode;
    private TextView txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);

        } else {
            setTheme(R.style.AppTheme);

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setSupportActionBar(findViewById(R.id.my_toolbar));

        switchMode = findViewById(R.id.switchBtn);
        txtView = findViewById(R.id.textView);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            switchMode.setChecked(true);
            txtView.setText("Dark mode");
        }
//        ConstraintLayout myLayout = findViewById(R.id.rootLayout);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Log.e("check", "-" + isChecked);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    txtView.setText("Dark mode");
//                    myLayout.setBackgroundResource(R.color.teal_700);
//                    myToolbar.setBackgroundResource(R.color.teal_700);

                    reset();
                } else {
                    Log.e("check", "-" + isChecked);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    txtView.setText("Light mode");
//                    myLayout.setBackgroundResource(R.color.green);
//                    myToolbar.setBackgroundResource(R.color.green);
                    reset();
                }
            }
        });
    }

    private void reset() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_activity, menu);
        MenuItem menuSetting = menu.findItem(R.id.menu_settings);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_back:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}