package com.example.monopoly;

import android.service.quicksettings.Tile;

import java.util.ArrayList;

public class Board{
    int min = 1, max=12, dice, numberOfPlayers=4;
    ArrayList<Integer> bankrupt = new ArrayList<>();
    ArrayList<Tiles> gameTiles = new ArrayList<Tiles>();

    Tiles t0 = new GameTile(0,"Pass GO", "Game", 0);
    Tiles t1 = new PropertyTile(1,"Mediterranean Avenue", "Property", 60, new int[]{2, 10, 30, 90, 160, 250});
    Tiles t2 = new GameTile(2,"Safehouse", "Game", 0);
    Tiles t3 = new PropertyTile(3,"Baltic Avenue", "Property", 60, new int[]{4, 20, 60, 180, 320, 450});
    Tiles t4 = new GameTile(4,"Income Tax", "Game", -200);
    Tiles t5 = new RailwayTile(5,"Reading Railroad", "Railway");
    Tiles t6 = new PropertyTile(6,"Oriental Avenue", "Property", 100, new int[]{6, 30, 90, 270, 400, 550});
    Tiles t7 = new GameTile(7,"Safehouse", "Game", 0);
    Tiles t8 = new PropertyTile(8,"Vermont Avenue", "Property", 100, new int[]{6, 30, 90, 270, 400, 550});
    Tiles t9 = new PropertyTile(9,"Connecticut Avenue", "Property", 120, new int[]{8, 40, 100, 300, 450, 600});
    Tiles t10 = new GameTile(10,"Just Visiting Jail", "Game", 0);
    Tiles t11 = new PropertyTile(11,"St Charles Place", "Property", 140, new int[]{10, 50, 150, 450, 625, 750});
    Tiles t12 = new UtilityTile(12,"Electric Company", "Utility");
    Tiles t13 = new PropertyTile(13,"States Avenue", "Property", 140, new int[]{10, 50, 150, 450, 625, 750});
    Tiles t14 = new PropertyTile(14,"Virginia Avenue", "Property", 160, new int[]{12, 60, 180, 500, 700, 900});
    Tiles t15 = new RailwayTile(15,"Pennsylvania Railroad", "Railway");
    Tiles t16 = new PropertyTile(16,"St James Place", "Property", 180, new int[]{14, 70, 200, 550, 750, 950});
    Tiles t17 = new GameTile(17,"Safehouse", "Game", 0);
    Tiles t18 = new PropertyTile(18,"Tennessee Avenue", "Property", 180, new int[]{14, 70, 200, 550, 750, 950});
    Tiles t19 = new PropertyTile(19,"New York Avenue", "Property", 200, new int[]{16, 80, 220, 600, 800, 1000});
    Tiles t20 = new GameTile(20,"Free Parking", "Game", 0);
    Tiles t21 = new PropertyTile(21,"Kentucky Avenue", "Property", 220, new int[]{18, 90, 250, 700, 875, 1050});
    Tiles t22 = new GameTile(22,"Safehouse", "Game", 0);
    Tiles t23 = new PropertyTile(23,"Indiana Avenue", "Property", 220, new int[]{18, 90, 250, 700, 875, 1050});
    Tiles t24 = new PropertyTile(24,"Illinois Avenue", "Property", 240, new int[]{20, 100, 300, 750, 925, 1100});
    Tiles t25 = new RailwayTile(25,"B & O Railroad", "Railway");
    Tiles t26 = new PropertyTile(26,"Atlantic Avenue", "Property", 260, new int[]{22, 110, 330, 800, 975, 1150});
    Tiles t27 = new PropertyTile(27,"Ventnor Avenue", "Property", 260, new int[]{22, 110, 330, 800, 975, 1150});
    Tiles t28 = new UtilityTile(28,"Electric Company", "Utility");
    Tiles t29 = new PropertyTile(29,"Marvin Gardens", "Property", 280, new int[]{24, 120, 360, 850, 1025, 1200});
    Tiles t30 = new GameTile(30,"Just Visiting Jail", "Game", 0);
    Tiles t31 = new PropertyTile(31,"Pacific Avenue", "Property", 300, new int[]{26, 130, 390, 900, 1100, 1275});
    Tiles t32 = new PropertyTile(32,"North Carolina Avenue", "Property", 300, new int[]{26, 130, 390, 900, 1100, 1275});
    Tiles t33 = new GameTile(33,"Safehouse", "Game", 0);
    Tiles t34 = new PropertyTile(34,"Pennsylvania Avenue", "Property", 320, new int[]{28, 150, 450, 1000, 1200, 1400});
    Tiles t35 = new RailwayTile(35,"Short Line", "Railway");
    Tiles t36 = new GameTile(36,"Safehouse", "Game", 0);
    Tiles t37 = new PropertyTile(37,"Park Place", "Property", 350, new int[]{35, 175, 500, 1100, 1300, 1500});
    Tiles t38 = new GameTile(38,"Income Tax", "Game", -150);
    Tiles t39 = new PropertyTile(39,"Boardwalk", "Property", 400, new int[]{50, 200, 600, 1400, 1700, 2000});


    private static Board b1 = null;

    private Board(){
        //Singleton
    }

    public static Board getBoard(){
        if(b1==null){
            b1 = new Board();
        }
        return b1;
    }

    public ArrayList<Tiles> initializeTiles(){
        gameTiles.add(t0);
        gameTiles.add(t1);
        gameTiles.add(t2);
        gameTiles.add(t3);
        gameTiles.add(t4);
        gameTiles.add(t5);
        gameTiles.add(t6);
        gameTiles.add(t7);
        gameTiles.add(t8);
        gameTiles.add(t9);
        gameTiles.add(t10);
        gameTiles.add(t11);
        gameTiles.add(t12);
        gameTiles.add(t13);
        gameTiles.add(t14);
        gameTiles.add(t15);
        gameTiles.add(t16);
        gameTiles.add(t17);
        gameTiles.add(t18);
        gameTiles.add(t19);
        gameTiles.add(t20);
        gameTiles.add(t21);
        gameTiles.add(t22);
        gameTiles.add(t23);
        gameTiles.add(t24);
        gameTiles.add(t25);
        gameTiles.add(t26);
        gameTiles.add(t27);
        gameTiles.add(t28);
        gameTiles.add(t29);
        gameTiles.add(t30);
        gameTiles.add(t31);
        gameTiles.add(t32);
        gameTiles.add(t33);
        gameTiles.add(t34);
        gameTiles.add(t35);
        gameTiles.add(t36);
        gameTiles.add(t37);
        gameTiles.add(t38);
        gameTiles.add(t39);
        return b1.gameTiles;
    }

    public int rollDice(){
        dice = (int)(Math.random()*(max-min+1)+min);
        return dice;
    }

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }
}
