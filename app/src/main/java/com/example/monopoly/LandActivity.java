package com.example.monopoly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class LandActivity extends AppCompatActivity {

    TextView placeText, priceText, rentText, ownerText;
    Button buyButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land);

        buyButton = (Button)findViewById(R.id.buyButton);
        backButton = (Button)findViewById(R.id.backButton);
        placeText = (TextView) findViewById(R.id.placeText);
        priceText = (TextView) findViewById(R.id.priceAmountText);
        rentText = (TextView) findViewById(R.id.ownerText);
        ownerText = (TextView) findViewById(R.id.rentAmountText);
    }
}