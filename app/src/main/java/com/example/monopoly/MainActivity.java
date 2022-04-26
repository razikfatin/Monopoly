package com.example.monopoly;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String p1Name, p2Name, p3Name, p4Name;
    EditText p1input, p2input, p3input, p4input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p1input = (EditText)findViewById(R.id.p1nameInput);
        p2input = (EditText)findViewById(R.id.p2nameInput);
        p3input = (EditText)findViewById(R.id.p3nameInput);
        p4input = (EditText)findViewById(R.id.p4nameInput);

        Button submitButton = (Button) findViewById(R.id.button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p1Name = p1input.getText().toString();
                p2Name = p2input.getText().toString();
                p3Name = p3input.getText().toString();
                p4Name = p4input.getText().toString();
                Intent intent = new Intent(getApplicationContext(), gameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("p1Name", p1Name);
                bundle.putString("p2Name", p2Name);
                bundle.putString("p3Name", p3Name);
                bundle.putString("p4Name", p4Name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}