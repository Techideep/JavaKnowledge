package com.game;

import java.util.ArrayList;
import java.util.List;

public class MainApp {
    
    public static void main(String[] args) {
        CricketPlayer player1 = new CricketPlayer("Deepti", 25 , .1, .4, .2, .1);
        List<CricketPlayer> players = new ArrayList<>();
        players.add(player1);
        Game cricket = new Cricket(players);
        System.out.println(cricket.getScore());
    }

}
