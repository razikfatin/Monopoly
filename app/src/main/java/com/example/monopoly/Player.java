package com.example.monopoly;

import android.service.quicksettings.Tile;

import java.util.ArrayList;

public class Player {
    int pid, position, numberOfRailways, numberOfUtility, totalAssets;
    private int wallet;
    boolean Rolled, turnOver;
    String name;
    ArrayList<Tiles> ownedTiles = new ArrayList<>();
    ArrayList<Tiles> mortgagedTiles = new ArrayList<>();

    public Player(int pid, String name){
        this.pid=pid;
        this.name=name;
        this.wallet=1500;
        this.numberOfRailways=0;
        this.numberOfUtility=0;
        this.position=0;
    }

    public int getWalletAmount(){
        return this.wallet;
    }

    public int getPosition(){
        return this.position;
    }

    public int getPid(){
        return this.pid;
    }

    public String getName(){ return this.name;}

    public int getTotalAssets(){
        return this.totalAssets;
    }

    public void addAmount(int amt){
        this.wallet+=amt;
    }

    public void deductAmount(int amt){
        this.wallet-=amt;
    }

    public Boolean hasRolled(){ return Rolled; }

    public Boolean isTurnOver(){ return turnOver; }

    public void buyProperty( PropertyTile pt){
        this.ownedTiles.add(pt);
        this.deductAmount(pt.price);
        this.totalAssets+=pt.mortgageValue;
    }

    public void buyRailway( RailwayTile rt){
        this.ownedTiles.add(rt);
        this.deductAmount(200);
        this.totalAssets+=100;
        this.numberOfRailways+=1;
    }

    public void buyUtility( UtilityTile ut){
        this.ownedTiles.add(ut);
        this.deductAmount(150);
        this.totalAssets+=75;
        this.numberOfUtility+=1;
    }
}
