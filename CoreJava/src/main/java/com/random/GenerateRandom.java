package com.random;

import java.util.Random;

public class GenerateRandom {
    
    public static void main(String args[]){
        Random random = new  Random(System.currentTimeMillis());
        int random_int=random.nextInt(100);
        int random_int1 = random.nextInt(100);
        System.out.println(random_int);
        System.out.println( random_int1);
        
        System.out.println(Math.random());
    }

}
