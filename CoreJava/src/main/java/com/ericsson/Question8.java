package com.ericsson;

public class Question8 {
    public static void main(String args[]){
        new Thread(new Two(),"print1").start();
    }
}

class One implements Runnable{
    public void run(){
        System.out.println(Thread.currentThread().getName());
    }
}

class Two implements Runnable{
    public void run(){
        new One().run();
        new Thread(new One(),"print2").run();
        new Thread(new One(),"print3").start();
    }
}
