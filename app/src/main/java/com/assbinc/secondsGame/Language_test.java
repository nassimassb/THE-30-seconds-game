package com.assbinc.secondsGame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Language_test extends AppCompatActivity {

    SharedPref sharedPref;
    SharedPreferences sharedPreferences, pref;
    Random random;
    String correctAnswer, incorrectAnswer, savedLanguage,chosenGame, difficulty;
    TextView tvQuestion, tvTimer, tvPoints, tvResult, tvLives;
    Button btn0, btn1, btn2, btn3, clickedBtn;
    CountDownTimer countDownTimer;
    long millisUntilFinished;
    int points, wrong, maxWrongAnswers;
    int numberOfQuestions, randomId;
    int[] btnIds;
    int correctAnswerPosition;
    ArrayList<String> incorrectAnswers;
    Gson gson;
    Type listType;
    Map<String, Words> wordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //check dark mode
        sharedPref = new SharedPref(this);
        setTheme(sharedPref.loadNightMode() ? R.style.darkTheme : R.style.lightTheme);

        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_test);

        correctAnswer = "";
        incorrectAnswer = "";
        tvTimer = findViewById(R.id.tvTimer);
        tvPoints = findViewById(R.id.tvPoints);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvResult = findViewById(R.id.tvResult2);
        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        tvLives = findViewById(R.id.tvLives);
        millisUntilFinished = 30100;
        points = 0;
        wrong = 0;
        maxWrongAnswers = 2;
        numberOfQuestions = 0;
        random = new Random();
        randomId = 0;
        btnIds = new int[]{R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3};
        correctAnswerPosition = 0;
        incorrectAnswers = new ArrayList<>();
        gson = new Gson();
        listType = new TypeToken<Map<String, Words>>() {}.getType();
//        listType = new TypeToken<List<Words>>(){}.getType();
//        wordsList = gson.fromJson(WordsJson.myWords, listType);
        chosenGame = getIntent().getStringExtra("chosenGame");
        pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        savedLanguage = pref.getString("My lang", "");
        sharedPreferences = getSharedPreferences("gameDifficulty", Activity.MODE_PRIVATE);
        difficulty = sharedPreferences.getString("difficulty", "");
        startGame();

    }

    //set saved language
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    //load saved language
    public void loadLocale(){
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = pref.getString("My lang", "");
        setLocale(language);
    }

    public void pauseGame(View view) {
        String chosenGame = getIntent().getStringExtra("chosenGame");
        sharedPreferences = getSharedPreferences("actualGame", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("actualGame", chosenGame);
        editor.commit();
        countDownTimer.cancel();
        Intent intent = new Intent(Language_test.this, PauseMenu.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
        Intent intent = new Intent(Language_test.this, ChooseLanguageGame.class);
        startActivity(intent);
        finish();
    }

    private void startGame() {

        tvTimer.setText("" + (millisUntilFinished / 1000) + "s");
        tvPoints.setText("" + points + "/" + numberOfQuestions);
        generateQuestion();

        countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("" + (millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                gameOver();
            }
        }.start();
    }

    private void generateQuestion() {

        numberOfQuestions++;
        //randomId = random.nextInt(wordsList.size());

        /*if(savedLanguage.equals("fr")){
            String question;
            switch (chosenGame){
                case "NlToEn":
                    question = wordsList.get(randomId).getNlWord();
                    tvQuestion.setText(question);
                    correctAnswer = wordsList.get(randomId).getFrWord();
                    break;
                case "EnToNl":
                    question = wordsList.get(randomId).getFrWord();
                    tvQuestion.setText(question);
                    correctAnswer = wordsList.get(randomId).getNlWord();
                    break;
                case "FrToEn":
                    question = wordsList.get(randomId).getEnWord();
                    tvQuestion.setText(question);
                    correctAnswer = wordsList.get(randomId).getFrWord();
                    break;
                case "EnToFr":
                    question = wordsList.get(randomId).getFrWord();
                    tvQuestion.setText(question);
                    correctAnswer = wordsList.get(randomId).getEnWord();
                    break;

            }
        }else if(savedLanguage.equals("nl")){
            String question;
            switch (chosenGame){
                case "NlToEn":
                    question = wordsList.get(randomId).getEnWord();
                    tvQuestion.setText(question);
                    correctAnswer = wordsList.get(randomId).getNlWord();
                    break;
                case "EnToNl":
                    question = wordsList.get(randomId).getNlWord();
                    tvQuestion.setText(question);
                    correctAnswer = wordsList.get(randomId).getFrWord();
                    break;
                case "FrToEn":
                    question = wordsList.get(randomId).getFrWord();
                    tvQuestion.setText(question);
                    correctAnswer = wordsList.get(randomId).getNlWord();
                    break;
                case "EnToFr":
                    question = wordsList.get(randomId).getNlWord();
                    tvQuestion.setText(question);
                    correctAnswer = wordsList.get(randomId).getEnWord();
                    break;
            }
        }else {
            String question;
            switch (chosenGame) {
                case "NlToEn":
                    question = wordsList.get(randomId).getNlWord();
                    tvQuestion.setText(question);
                    correctAnswer = wordsList.get(randomId).getEnWord();
                    break;
                case "EnToNl":
                    question = wordsList.get(randomId).getEnWord();
                    tvQuestion.setText(question);
                    correctAnswer = wordsList.get(randomId).getNlWord();
                    break;
                case "FrToEn":
                    question = wordsList.get(randomId).getFrWord();
                    tvQuestion.setText(question);
                    correctAnswer = wordsList.get(randomId).getEnWord();
                    break;
                case "EnToFr":
                    question = wordsList.get(randomId).getEnWord();
                    tvQuestion.setText(question);
                    correctAnswer = wordsList.get(randomId).getFrWord();
                    break;
            }
        }
        correctAnswerPosition = random.nextInt(4);

        ((Button) findViewById(btnIds[correctAnswerPosition])).setText(correctAnswer);*/

        tvLives.setText((maxWrongAnswers+1) - wrong + "");

        setIncorrectAnswers();

    }

    private void setIncorrectAnswers(){
        /*while(true) {
            if (incorrectAnswers.size() > 3) {
                break;
            }
            randomId = random.nextInt(wordsList.size());

            if(savedLanguage.equals("fr")){
                switch (chosenGame){
                    case "NlToEn":
                    case "FrToEn":
                        incorrectAnswer = wordsList.get(randomId).getFrWord();
                        break;
                    case "EnToNl":
                        incorrectAnswer = wordsList.get(randomId).getNlWord();
                        break;
                    case "EnToFr":
                        incorrectAnswer = wordsList.get(randomId).getEnWord();
                        break;
                }
            }else if(savedLanguage.equals("nl")){
                switch (chosenGame){
                    case "NlToEn":
                    case "FrToEn":
                        incorrectAnswer = wordsList.get(randomId).getNlWord();
                        break;
                    case "EnToNl":
                        incorrectAnswer = wordsList.get(randomId).getFrWord();
                        break;
                    case "EnToFr":
                        incorrectAnswer = wordsList.get(randomId).getEnWord();
                        break;
                }
            }else{
                switch (chosenGame){
                    case "NlToEn":
                    case "FrToEn":
                        incorrectAnswer = wordsList.get(randomId).getEnWord();
                        break;
                    case "EnToNl":
                        incorrectAnswer = wordsList.get(randomId).getNlWord();
                        break;
                    case "EnToFr":
                        incorrectAnswer = wordsList.get(randomId).getFrWord();
                        break;
                }
            }

            if(incorrectAnswer == correctAnswer)
                continue;
            if(incorrectAnswers.contains(incorrectAnswer))
                continue;
            incorrectAnswers.add(incorrectAnswer);
        }

        for(int i = 0; i< 4; i++){
            if(i == correctAnswerPosition)
                continue;

            ((Button) findViewById(btnIds[i])).setText(incorrectAnswers.get(i));

        }

        incorrectAnswers.clear();*/
    }
    private void gameOver() {
        /*if(countDownTimer != null)
        {
            countDownTimer.cancel();
        }
        btn0.setClickable(false);
        btn1.setClickable(false);
        btn2.setClickable(false);
        btn3.setClickable(false);
        Intent intent = new Intent(Language_test.this, GameOver.class);
        intent.putExtra("points", points);
        startActivity(intent);
        finish();*/
    }

    public void chooseAnswer(View view) {

        //Log.i("gson", "size: " + wordsList.size() + " | random: " + randomId + " | GSON: " + wordsList.get(randomId).getFrWord());

        clickedBtn = (Button) view;

        if (!(view instanceof ImageButton)) {
            String answer = clickedBtn.getText().toString();
            String strCorrect = getResources().getString(R.string.correct);
            String strWrong = getResources().getString(R.string.wrong);

            if (answer.equalsIgnoreCase(correctAnswer)) {
                points++;

                //change the color of the clicked button to green
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    clickedBtn.setBackground(getResources().getDrawable(R.drawable.rounded_green));
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //set its initial color
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            clickedBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
                        }
                    }
                }, 500);

                tvResult.setText(strCorrect);
            } else {
                if (wrong < maxWrongAnswers) {
                    //change the color of the clicked button to green
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        clickedBtn.setBackground(getResources().getDrawable(R.drawable.rounded_red));
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //set its initial color
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                clickedBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
                            }
                        }
                    }, 500);
                    tvResult.setText(strWrong);
                    wrong++;
                } else {
                    gameOver();
                }
            }

            tvPoints.setText(points + "/" + numberOfQuestions);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //make the text disappear after 1s
                    tvResult.setText("");
                }
            }, 1000);
            generateQuestion();
        }
    }
}