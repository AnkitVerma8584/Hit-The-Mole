package com.example.hitthemole;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView[][] btn = new TextView[3][3];
    private int hit = 0, attempt;
    private TextView h, life, time;
    private CountDownTimer countDownTimer;
    private TextView s;
    private String hearts = "\u2764\u2764\u2764\u2764\u2764", myName;
    private int level = 0, timeTaken = 0;
    private boolean gameOver;
    private final String[] LEVEL = {"Easy", "Medium", "High"};
    private final int[] levels = {700, 600, 500};
    private List<Scores> scores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        level = getIntent().getIntExtra("Level", 0);
        myName = getIntent().getStringExtra("Name");
        h = findViewById(R.id.p1);
        time = findViewById(R.id.time);
        life = findViewById(R.id.p2);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String btn_id = "btn" + i + j;
                int resId = getResources().getIdentifier(btn_id, "id", getPackageName());
                btn[i][j] = findViewById(resId);
                btn[i][j].setEnabled(false);
                btn[i][j].setOnClickListener(this);
            }
        }
        s = findViewById(R.id.start);
        s.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (((TextView) v).getText().toString().equals("M")) {
            disableAll();
            h.setText("Hits : " + ++hit);
        } else if (((TextView) v).getText().toString().equals("")) {
            disableAll();
            life.setText(hearts.substring(0, attempt - 1));
            attempt--;
        }

        if (((TextView) v).getText().toString().equals("Start")) {
            reset();
            startTime();
            clearViews();
            v.setEnabled(false);
        }
    }

    private void startTime() {
        countDownTimer = new CountDownTimer((2 * 60 * 1000), 1000) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                timeTaken++;
                long timeLeft = millisUntilFinished / 1000;
                int sec = (int) (timeLeft % 60);
                int min = (int) (timeLeft / 60);
                if (timeLeft < 30)
                    time.setTextColor(Color.RED);
                time.setText(String.format("%02d:%02d", min, sec));
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                time.setText("00:00");
                gameOver = true;
                gameOver(2, 0);
            }
        };
        countDownTimer.start();
    }

    @SuppressLint("SetTextI18n")
    private void reset() {
        gameOver = false;
        hit = 0;
        h.setText("Hits : " + hit);
        attempt = 5;
        timeTaken = 0;
        time.setText("02:00");
        time.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        life.setText(hearts);
    }

    private void clear() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                btn[i][j].setText("");
                btn[i][j].setEnabled(true);
                btn[i][j].setBackgroundResource(R.drawable.hole);
            }
        }
    }

    private void disableAll() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                btn[i][j].setEnabled(false);
            }
        }
    }

    private void clearViews() {
        clear();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!gameOver)
                    if (attempt > 0)
                        putViews();
                    else {
                        gameOver(timeTaken / 60, timeTaken % 60);
                    }
            }
        }, 180);
    }

    @SuppressLint("SetTextI18n")
    private void putViews() {
        final int ii = new Random().nextInt(3);
        final int jj = new Random().nextInt(3);
        btn[ii][jj].setBackgroundResource(R.drawable.mole);
        btn[ii][jj].setText("M");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                clearViews();
            }
        }, levels[level]);
    }

    @SuppressLint("SetTextI18n")
    private void gameOver(int i, int j) {
        countDownTimer.cancel();
        s.setEnabled(true);
        showMyDialog(i, j);
    }

    @SuppressLint("DefaultLocale")
    private void showMyDialog(int min, int sec) {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.CustomDialogueTheme);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View custom_dialogue = layoutInflater.inflate(R.layout.game_over_dialog, null);
        TextView name = custom_dialogue.findViewById(R.id.name);
        TextView hits = custom_dialogue.findViewById(R.id.hits);
        TextView life = custom_dialogue.findViewById(R.id.life);
        TextView time = custom_dialogue.findViewById(R.id.time);
        TextView lvl = custom_dialogue.findViewById(R.id.level);
        Button btn = custom_dialogue.findViewById(R.id.view_scores);
        name.append(myName);
        hits.append(String.valueOf(hit));
        life.append(String.valueOf(attempt));
        time.append(String.format("%02d:%02d", min, sec));
        lvl.append(LEVEL[level]);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.setContentView(custom_dialogue);
        dialog.show();
    }

    private void saveScores() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("Scores", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(scores);
                editor.putString(LEVEL[level], json);
                editor.apply();
            }
        }).start();

    }

    private void loadScores() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("Shared Preference saved", MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString(LEVEL[level], null);
                Type type = new TypeToken<ArrayList<Scores>>() {
                }.getType();
                scores = gson.fromJson(json, type);
            }
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadScores();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveScores();
    }
    /*private void sort(){
        Collections.sort(myList, new Comparator<EmployeeClass>(){
            public int compare(EmployeeClass obj1, EmployeeClass obj2) {
                // ## Ascending order
                return obj1.firstName.compareToIgnoreCase(obj2.firstName); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
            }
        });
    }*/
}