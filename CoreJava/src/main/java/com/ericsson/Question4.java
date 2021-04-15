package com.ericsson;

public class Question4 {
    public static void main(String args[]){
        System.out.println(fun());
    }

    private static int fun() {
        //static
        int x=0;
        return ++x;
    }
}
