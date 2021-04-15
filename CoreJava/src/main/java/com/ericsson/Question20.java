package com.ericsson;

public class Question20 {
    public static void main(String args[]){
        assign(1,"Hello");

    }

    private static <T> void assign(T a, T b) {
        a=b;
        System.out.print(a);
    }
}
