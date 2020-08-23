package com.example.hitthemole;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Start extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int levels = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final EditText editText = findViewById(R.id.editText);
        Spinner spinner = findViewById(R.id.spinner);
        Button button = findViewById(R.id.button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString().trim();
                if (name.isEmpty())
                    editText.setError("Empty field");
                else {
                    editText.setError(null);
                    Intent intent = new Intent(Start.this, MainActivity.class);
                    intent.putExtra("Level", levels);
                    intent.putExtra("Name", name);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        levels = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}