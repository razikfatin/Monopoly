package com.example.monopoly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class gameActivity extends AppCompatActivity {

    Button rollDice, finishTurn, buildButton, sellButton, mortgageButton, redeemButton, tradeButton;
    int dice, p1Wallet, p2Wallet, p3Wallet, p4Wallet, currentPlayer;
    String p1Name, p2Name, p3Name, p4Name;
    TextView p1n, p2n, p3n, p4n, p1w, p2w, p3w, p4w, diceVal;
    Board gameBoard = Board.getBoard();
    ArrayList<Tiles> gameTiles = gameBoard.initializeTiles();
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
        buildButton = (Button)findViewById(R.id.buildButton);
        mortgageButton = (Button)findViewById(R.id.mortgageButton);
        redeemButton = (Button)findViewById(R.id.redeemButton);
        tradeButton = (Button)findViewById(R.id.tradeButton);
        sellButton = (Button)findViewById(R.id.sellButton);

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

        playerDB.child("Player 1").child("Position").setValue(0);
        playerDB.child("Player 2").child("Position").setValue(0);
        playerDB.child("Player 3").child("Position").setValue(0);
        playerDB.child("Player 4").child("Position").setValue(0);

        updateWallets();
        currentPlayer=1;
        insertPlacesIntoDB();
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
                interact(players[currentPlayer-1].getPosition(), players[currentPlayer-1], dice);
            }
            else
                Toast.makeText(getApplicationContext(),"Already Rolled !",Toast.LENGTH_SHORT).show();
        });

        buildButton.setOnClickListener(v -> {
            buildHouseOnProperty();
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

    public int movePlayer(Player p, int spaces){
        int x = p.getPosition() + spaces;
        if(x > 39)
            x = x - 40;
        p.position=x;
        if(p.getPid() == 1)
            playerDB.child("Player 1").child("Position").setValue(p.getPosition());
        else if(p.getPid() == 2)
            playerDB.child("Player 2").child("Position").setValue(p.getPosition());
        else if(p.getPid() == 3)
            playerDB.child("Player 3").child("Position").setValue(p.getPosition());
        else if(p.getPid() == 4)
            playerDB.child("Player 4").child("Position").setValue(p.getPosition());

        return p.getPosition();
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

    public void interact( int pos, Player p, int dice){

        int r = 0;

        if(gameTiles.get(pos).type == "Game"){
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(gameActivity.this);
            String title="You have landed on : " + gameTiles.get(pos).title;
            builder.setTitle(title);
            //builder.setMessage("Do you want to buy it ?");
            builder
                    .setPositiveButton(
                            "OK",
                            new DialogInterface
                                    .OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    if(gameTiles.get(pos).tileID == 0)
                                        dialog.cancel();
                                    else if(gameTiles.get(pos).tileID == 4){
                                        p.deductAmount(200);
                                        dialog.cancel();
                                    }
                                    else if(gameTiles.get(pos).tileID == 38){
                                        p.deductAmount(150);
                                        dialog.cancel();
                                    }
                                    else if(gameTiles.get(pos).tileID == 30){
                                        p.position=10;
                                        p.deductAmount(100);
                                        dialog.cancel();
                                    }
                                }
                            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if(gameTiles.get(pos).type == "Property" && gameTiles.get(pos).getOwner() == 0){
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(gameActivity.this);
            String title="You have landed on : " + gameTiles.get(pos).title;
            builder.setTitle(title);
            builder.setMessage("Do you want to buy it ?");
            builder
                    .setPositiveButton(
                            "Yes",
                            new DialogInterface
                                    .OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    PropertyTile pt = (PropertyTile)gameTiles.get(pos).getTile();
                                    p.buyProperty(pt);
                                    gameTiles.get(pos).setOwner(p.getPid());
                                    placesDB.child("Property").child(pt.title).child("Owner").setValue(p.getPid());
                                    dialog.cancel();
                                }
                            });
            builder
                    .setNegativeButton(
                            "No",
                            new DialogInterface
                                    .OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if(gameTiles.get(pos).type == "Railway" && gameTiles.get(pos).getOwner() == 0){
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(gameActivity.this);
            String title="You have landed on : " + gameTiles.get(pos).title;
            builder.setTitle(title);
            builder.setMessage("Do you want to buy it ?");
            builder
                    .setPositiveButton(
                            "Yes",
                            new DialogInterface
                                    .OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    RailwayTile rt = (RailwayTile)gameTiles.get(pos).getTile();
                                    p.buyRailway(rt);
                                    gameTiles.get(pos).setOwner(p.getPid());
                                    placesDB.child("Railway").child(rt.title).child("Owner").setValue(p.getPid());
                                    dialog.cancel();
                                }
                            });
            builder
                    .setNegativeButton(
                            "No",
                            new DialogInterface
                                    .OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if(gameTiles.get(pos).type == "Utility" && gameTiles.get(pos).getOwner() == 0){
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(gameActivity.this);
            String title="You have landed on : " + gameTiles.get(pos).title;
            builder.setTitle(title);
            builder.setMessage("Do you want to buy it ?");
            builder
                    .setPositiveButton(
                            "Yes",
                            new DialogInterface
                                    .OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    UtilityTile ut = (UtilityTile)gameTiles.get(pos).getTile();
                                    p.buyUtility(ut);
                                    gameTiles.get(pos).setOwner(p.getPid());
                                    placesDB.child("Utility").child(ut.title).child("Owner").setValue(p.getPid());
                                    dialog.cancel();
                                }
                            });
            builder
                    .setNegativeButton(
                            "No",
                            new DialogInterface
                                    .OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(gameActivity.this);
            String title="You have landed on : " + gameTiles.get(pos).title;
            builder.setTitle(title);
            int x = gameTiles.get(pos).getOwner();
            int n,rent = 0;
            if(gameTiles.get(pos).type=="Property"){
                PropertyTile pt = (PropertyTile)gameTiles.get(pos).getTile();
                n = pt.numberOfHouses;
                rent = pt.getRent(n);
                String amt = Integer.toString(rent);
                String req="Please pay $" + amt + " to " + players[x-1].getName();
                builder.setMessage(req);
                r=rent;
            }
            else if(gameTiles.get(pos).type=="Railway"){
                if(p.numberOfRailways == 1)
                    rent=25;
                else if(p.numberOfRailways == 2)
                    rent=50;
                else if(p.numberOfRailways == 3)
                    rent=100;
                else if(p.numberOfRailways == 4)
                    rent=200;
                String amt = Integer.toString(rent);
                String req="Please pay $" + amt + " to " + players[currentPlayer-1].getName();
                builder.setMessage(req);
                r=rent;
            }
            else if(gameTiles.get(pos).type=="Utility"){
                if(p.numberOfUtility == 1)
                    rent = dice * 4;
                else if(p.numberOfUtility == 2)
                    rent = dice * 10;
                String amt = Integer.toString(rent);
                String req="Please pay $" + amt + " to " + players[currentPlayer-1].getName();
                builder.setMessage(req);
                r=rent;
            }


            int finalR = r;
            builder
                    .setPositiveButton(
                            "OK",
                            new DialogInterface
                                    .OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    players[x-1].addAmount(finalR);
                                    p.deductAmount(finalR);
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void insertPlacesIntoDB(){
        placesDB.child("Property").child("Mediterranean Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Baltic Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Oriental Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Vermont Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Connecticut Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("St Charles Place").child("Owner").setValue(0);
        placesDB.child("Property").child("States Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Virginia Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("St James Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Tennessee Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("New York Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Kentucky Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Indiana Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Illinois Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Atlantic Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Ventnor Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Marvin Gardens").child("Owner").setValue(0);
        placesDB.child("Property").child("Pacific Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("North Carolina Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Pennsylvania Avenue").child("Owner").setValue(0);
        placesDB.child("Property").child("Park Place").child("Owner").setValue(0);
        placesDB.child("Property").child("Boardwalk").child("Owner").setValue(0);
        placesDB.child("Railway").child("Reading Railroad").child("Owner").setValue(0);
        placesDB.child("Railway").child("Pennsylvania Railroad").child("Owner").setValue(0);
        placesDB.child("Railway").child("B & O Railroad").child("Owner").setValue(0);
        placesDB.child("Railway").child("Short Line").child("Owner").setValue(0);
        placesDB.child("Utility").child("Electric Company").child("Owner").setValue(0);
        placesDB.child("Utility").child("Water Works").child("Owner").setValue(0);
    }

    public void buildHouseOnProperty(){
        PropertyTile pt;
        final int[] pd = new int[1];
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(gameActivity.this);
        String title="Enter ID of property :";
        builder.setTitle(title);
        //builder.setMessage("Do you want to buy it ?");
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.buildlayout,
                        null);
        builder.setView(customLayout);

        builder
                .setPositiveButton(
                        "Build",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                EditText editText = customLayout.findViewById(R.id.placeNameInput);
                                pd[0] = Integer.parseInt(editText.getText().toString());
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Tiles t;
        t = gameTiles.get(pd[0]).getTile();
        pt=(PropertyTile)t;
        if(pt.numberOfHouses < 5)
            pt.numberOfHouses+=1;
        else
            Toast.makeText(getApplicationContext(),"Max houses built",Toast.LENGTH_SHORT).show();
    }
}