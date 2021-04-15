package com.random;

public class CutomizedRandomNumberGenerator {
    
    int max;
    int last;
    
    public CutomizedRandomNumberGenerator(int max){
        this.max = max ;
        this.last = (int)System.currentTimeMillis() % max;
    }
    
    public int nextInt(){
        last = ( last *32719 +29) % 32749;
        return last % max;
    }
    
    
    
    public static void main(String[] args) {
        CutomizedRandomNumberGenerator random = new CutomizedRandomNumberGenerator(20);
        for(int i=0; i< 5 ;i++){
            System.out.println(random.nextInt());
        }
    }

}
