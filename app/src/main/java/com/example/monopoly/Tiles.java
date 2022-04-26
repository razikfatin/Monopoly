package com.example.monopoly;

public class Tiles {
    String title, type;
    int tileID;
    private int owner;

    public Tiles(int tileID, String title, String type){
        this.tileID=tileID;
        this.title=title;
        this.type=type;
        this.owner=0;
    }

    public void setOwner(int pid){
        this.owner=pid;
    }

    public int getOwner(){
        return this.owner;
    }
}

class PropertyTile extends Tiles{

    int price, mortgageValue, redeemAmount;
    int[] rent = new int[6];

    public PropertyTile(int tileID, String title, String type, int price, int[] rent) {
        super(tileID, title, type);
        this.price=price;
        this.mortgageValue=price/2;
        this.redeemAmount = this.mortgageValue + price/10;
        this.rent=rent;
    }
}

class UtilityTile extends Tiles{

    int price = 150, mortgageValue = 75, redeemAmount = 90;
    public UtilityTile(int tileID, String title, String type) {
        super(tileID, title, type);
    }
}

class GameTile extends Tiles{
    int amount;
    public GameTile(int tileID, String title, String type, int amount) {
        super(tileID, title, type);
        this.amount = amount;
    }
}

class RailwayTile extends Tiles{
    int price = 200, mortgageValue = 100, redeemAmount = 110;
    int[] rent = new int[]{25, 50, 100, 200};
    public RailwayTile(int tileID, String title, String type) {
        super(tileID, title, type);
    }
}
