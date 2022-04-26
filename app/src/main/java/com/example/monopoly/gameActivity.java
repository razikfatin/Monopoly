package com.example.monopoly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class gameActivity extends AppCompatActivity {

    Button rollDice, finishTurn;
    int dice, p1Wallet, p2Wallet, p3Wallet, p4Wallet, currentPlayer;
    String p1Name, p2Name, p3Name, p4Name;
    TextView p1n, p2n, p3n, p4n, p1w, p2w, p3w, p4w, diceVal;
    Board gameBoard = Board.getBoard();
    Player[] players = new Player[4];
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://monopoly-b9d49-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference playerDB = database.getReference("Players");
    DatabaseReference placesDB = database.getReference("Places");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle bundle = getIntent().getExtras();

        rollDice = (Button)findViewById(R.id.rollDice);
        finishTurn = (Button)findViewById(R.id.endTurn);

        diceVal = (TextView) findViewById(R.id.diceValue);

        p1n = (TextView) findViewById(R.id.p1NameView);
        p1w = (TextView) findViewById(R.id.p1WalletView);
        p2n = (TextView) findViewById(R.id.p2NameView);
        p2w = (TextView) findViewById(R.id.p2WalletView);
        p3n = (TextView) findViewById(R.id.p3NameView);
        p3w = (TextView) findViewById(R.id.p3WalletView);
        p4n = (TextView) findViewById(R.id.p4NameView);
        p4w = (TextView) findViewById(R.id.p4WalletView);

        p1Name=bundle.getString("p1Name");
        p2Name=bundle.getString("p2Name");
        p3Name=bundle.getString("p3Name");
        p4Name=bundle.getString("p4Name");

        p1n.setText(p1Name);
        p2n.setText(p2Name);
        p3n.setText(p3Name);
        p4n.setText(p4Name);

        players[0] = new Player(1,p1Name);
        players[1] = new Player(2,p2Name);
        players[2] = new Player(3,p3Name);
        players[3] = new Player(4,p4Name);

        playerDB.child("Player 1").child("Name").setValue(p1Name);
        playerDB.child("Player 2").child("Name").setValue(p2Name);
        playerDB.child("Player 3").child("Name").setValue(p3Name);
        playerDB.child("Player 4").child("Name").setValue(p4Name);
        //playerDB.child("Player 1").child("Wallet").push().setValue(players[0].getWalletAmount());
        //playerDB.child("Player 2").child("Wallet").push().setValue(players[1].getWalletAmount());
        //playerDB.child("Player 3").child("Wallet").push().setValue(players[2].getWalletAmount());
        //playerDB.child("Player 4").child("Wallet").push().setValue(players[3].getWalletAmount());

        updateWallets();
        currentPlayer=1;
        gameBoard.initializeTiles();
        startGame();
    }
    public void updateWallets(){
        String val1, val2, val3, val4;
        p1Wallet = players[0].getWalletAmount();
        p2Wallet = players[1].getWalletAmount();
        p3Wallet = players[2].getWalletAmount();
        p4Wallet = players[3].getWalletAmount();
        val1 = Integer.toString(p1Wallet);
        val2 = Integer.toString(p2Wallet);
        val3 = Integer.toString(p3Wallet);
        val4 = Integer.toString(p4Wallet);
        p1w.setText(val1);
        p2w.setText(val2);
        p3w.setText(val3);
        p4w.setText(val4);
        playerDB.child("Player 1").child("Wallet").setValue(p1Wallet);
        playerDB.child("Player 2").child("Wallet").setValue(p2Wallet);
        playerDB.child("Player 3").child("Wallet").setValue(p3Wallet);
        playerDB.child("Player 4").child("Wallet").setValue(p4Wallet);
        //Map<String, String> userData = new HashMap<String, String>();
        //userData.put("Wallet", val1);
        //playerDB.child("Player1").push().setValue(userData);
        //playerDB.child("Player 1").child("Wallet").push().setValue(p1Wallet);
    }

    public void startGame(){
        //if(isBroke(players[currentPlayer]))
        //    continue;
        //highlightCurrentPlayer(currentPlayer);
        highlightCurrentPlayer(currentPlayer);

        rollDice.setOnClickListener(v -> {
            if(!players[currentPlayer-1].hasRolled()){
                //highlightCurrentPlayer(currentPlayer);
                players[currentPlayer-1].Rolled=true;
                dice = gameBoard.rollDice();
                String val = Integer.toString(dice);
                diceVal.setText(val);
                movePlayer(players[currentPlayer-1], dice);
            }
            else
                Toast.makeText(getApplicationContext(),"Already Rolled !",Toast.LENGTH_SHORT).show();
        });

        finishTurn.setOnClickListener(v -> {
            if(players[currentPlayer-1].hasRolled()) {
                players[currentPlayer-1].turnOver = true;
                if (currentPlayer == 4)
                    currentPlayer = 1;
                else
                    currentPlayer += 1;
                updateWallets();
                highlightCurrentPlayer(currentPlayer);
                players[currentPlayer-1].Rolled=false;
                players[currentPlayer-1].turnOver=false;
            }
            else
                Toast.makeText(getApplicationContext(),"Play first!",Toast.LENGTH_SHORT).show();
        });
    }

    public Boolean isBroke(int p){
        if(players[p].getTotalAssets()<=0)
            return true;
        else
            return false;
    }

    public void movePlayer(Player p, int spaces){
        int x = p.position + spaces;
        if(x > 39)
            x = x - 39;
        p.position=x;
    }

    public void highlightCurrentPlayer(int pid){
        if(pid == 1){
            p1n.setTextColor(Color.RED);
            p1w.setTextColor(Color.RED);
            p2n.setTextColor(Color.BLACK);
            p2w.setTextColor(Color.BLACK);
            p3n.setTextColor(Color.BLACK);
            p3w.setTextColor(Color.BLACK);
            p4n.setTextColor(Color.BLACK);
            p4w.setTextColor(Color.BLACK);
        }
        else if(pid == 2){
            p1n.setTextColor(Color.BLACK);
            p1w.setTextColor(Color.BLACK);
            p2n.setTextColor(Color.RED);
            p2w.setTextColor(Color.RED);
            p3n.setTextColor(Color.BLACK);
            p3w.setTextColor(Color.BLACK);
            p4n.setTextColor(Color.BLACK);
            p4w.setTextColor(Color.BLACK);
        }
        else if(pid == 3){
            p1n.setTextColor(Color.BLACK);
            p1w.setTextColor(Color.BLACK);
            p2n.setTextColor(Color.BLACK);
            p2w.setTextColor(Color.BLACK);
            p3n.setTextColor(Color.RED);
            p3w.setTextColor(Color.RED);
            p4n.setTextColor(Color.BLACK);
            p4w.setTextColor(Color.BLACK);
        }
        else if(pid == 4){
            p1n.setTextColor(Color.BLACK);
            p1w.setTextColor(Color.BLACK);
            p2n.setTextColor(Color.BLACK);
            p2w.setTextColor(Color.BLACK);
            p3n.setTextColor(Color.BLACK);
            p3w.setTextColor(Color.BLACK);
            p4n.setTextColor(Color.RED);
            p4w.setTextColor(Color.RED);
        }
    }
}