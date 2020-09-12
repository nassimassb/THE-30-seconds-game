package com.assbinc.secondsGame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Switch darkModeToggle;
        Switch notificationToggle;

        sharedPref = new SharedPref(this);

        //set dark theme that we configured
        setTheme(sharedPref.loadNightMode()? R.style.darkTheme: R.style.lightTheme);

        super.onCreate(savedInstanceState);
        loadLocale(); //load selected language
        setContentView(R.layout.settings);

        //allow notifications or not
        /*notificationToggle = findViewById(R.id.notifToggle);

        notificationToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){

            }else{

            }
        });*/
        //switch between dark and light mode
        darkModeToggle = findViewById(R.id.darkModeToggle);
        if(sharedPref.loadNightMode() == true){
            darkModeToggle.setChecked(true);
        }
        darkModeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sharedPref.setNightMode(true);
                    recreate();
                }else{
                    sharedPref.setNightMode(false);
                    recreate();
                }
//                Log.d("checked", "checked: " + isChecked + " | boolean: " + isDarkMode);
            }
        });

        //change language
        Button changeLang = findViewById(R.id.btnChangeLanguage);
        changeLang.setOnClickListener(v -> {
            btnAnimation(v);
            showChangeLanguageDialog();
        });
    }

    public void goMyAccount(View view){

        btnAnimation(view);
        Intent intent = new Intent(Settings.this, MyAccount.class);
        intent.putExtra("gameover", "settings");
        startActivity(intent);
        finish();
    }



    private void showChangeLanguageDialog(){
        //Array of languages to display in alert dialog
        final String[] listOfLang = {"Français", "Nederlands", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Settings.this,sharedPref.loadNightMode()? R.style.Theme_AppCompat_DayNight_Dialog: R.style.Theme_AppCompat_Light_Dialog);
        mBuilder.setSingleChoiceItems(listOfLang, -1, (dialog, i) -> {
            if (i == 0){
                //set french
                setLocale("fr");
                recreate();
            }
            if (i == 1){
                //set french
                setLocale("nl");
                recreate();
            }
            if (i == 2){
                //set french
                setLocale("en");
                recreate();
            }
            //dismiss dialog when language selected
            dialog.dismiss();
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    //change language
    private void setLocale(String lang) {
        Locale locale;
        if(lang.equals("")){ //if there's no saved language
            locale = new Locale(Locale.getDefault().getLanguage()); //get default language of the device
        }else{
            locale = new Locale(lang);
        }        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        //save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My lang", lang);
        editor.apply();
    }

    //load saved language
    public void loadLocale(){
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = pref.getString("My lang", "");
        setLocale(language);
    }

    public void chooseDifficulty(View view){
        btnAnimation(view);

        SharedPreferences.Editor editor = getSharedPreferences("gameDifficulty", MODE_PRIVATE).edit();
        String difficulty = "";

        switch(view.getId()){
            case (R.id.difficultyEasyBtn):
                //save data to shared preferences
                editor.putString("difficulty", "easy");
                editor.apply();
                difficulty = getResources().getString(R.string.difficultyEasy);

                break;
            case (R.id.difficultyMediumBtn):
                editor.putString("difficulty", "medium");
                editor.apply();
                difficulty = getResources().getString(R.string.difficultyMedium);

                break;
            case (R.id.difficultyHardBtn):
                editor.putString("difficulty", "hard");
                editor.apply();
                difficulty = getResources().getString(R.string.difficultyHard);

                break;
        }


        Toast.makeText(Settings.this, getResources().getString(R.string.changeDfficultyTo) + difficulty ,Toast.LENGTH_SHORT).show();
    }

    public static void btnAnimation(View view){
        //little animation when button is clicked
        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(500);
        view.startAnimation(animation1);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        SharedPreferences sharedPreferences = getSharedPreferences("activity", Activity.MODE_PRIVATE);
        String actual = sharedPreferences.getString("activity","");

        if(actual.equalsIgnoreCase("main")){
            Intent intent = new Intent(Settings.this, MainActivity.class);
            startActivity(intent);
        }else if(actual.equalsIgnoreCase("pause")){
            Intent intent = new Intent(Settings.this, PauseMenu.class);
            startActivity(intent);
        }
        finish();
    }

}
