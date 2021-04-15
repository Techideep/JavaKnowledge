package com.game;

import java.util.List;

public class Cricket implements Game {
    
    List<CricketPlayer> players; 
    
    public Cricket(List<CricketPlayer> players) {
        this.players = players;
    }


    @Override
    public double getScore() {
        return players.stream().mapToDouble(a-> a.getScore()).sum();
    }

}
