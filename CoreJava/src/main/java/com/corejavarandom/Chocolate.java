package com.corejavarandom;

    public class Chocolate {

        public static void main(String args[]){
             int rs=40;
            int noOfChocolates=rs;
            while (rs>2){
                noOfChocolates=noOfChocolates+rs/3;
                rs= (rs/3) +(rs%3);
            }
            System.out.print(noOfChocolates);
        }
    }
