package com.game;

public class CricketPlayer extends Player {
    
    double prob0;
    double prob4;
    double prob6;
    double probout;

    public CricketPlayer(String name , int age,double prob0, double prob4, double prob6, double probout) {
        super(name,age);
        this.prob0 = prob0;
        this.prob4 = prob4;
        this.prob6 = prob6;
        this.probout = probout;
    }
    
    public double getScore(){
        return prob4 * 100 * 4 + prob6 * 100 * 6;
    }
    
}
