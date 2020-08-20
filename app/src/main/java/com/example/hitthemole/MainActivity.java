package com.example.hitthemole;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] btn = new Button[3][3];
    private int hit = 0, attempt = 30;
    private TextView h, m, mi;
    private Button s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        h = findViewById(R.id.p1);
        m = findViewById(R.id.p2);
        mi = findViewById(R.id.miss);

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

        if (((Button) v).getText().toString().equals("M")) {
            disableAll();
            h.setText("Hit : " + ++hit);
        } else if (((Button) v).getText().toString().equals("Start")) {
            play();
            v.setEnabled(false);
        } else {
            disableAll();
        }
    }

    @SuppressLint("SetTextI18n")
    private void play() {
        clear();
        hit = 0;
        attempt = 30;
        h.setText("Hit : 0");
        m.setText("Attempts : 30");
        mi.setText(null);
        new CountDownTimer(18600, 600) {
            @Override
            public void onTick(long millisUntilFinished) {
                clear();
                int ii = new Random().nextInt(3);
                int jj = new Random().nextInt(3);
                m.setText("Attempts : " + --attempt);
                btn[ii][jj].setBackgroundResource(R.drawable.mole);
                btn[ii][jj].setText("M");
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                mi.setText("Missed : " + (30 - hit));
                disableAll();
                s.setEnabled(true);
            }
        }.start();
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
}