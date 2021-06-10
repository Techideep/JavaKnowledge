package com.leetcode.may;

public class NonDecreasingArray {
    public static void main(String args[]){
        int nums[]={-1,4,2,3};
        boolean flag =checkPossibility(nums);
            System.out.println(flag);

    }
    public static boolean checkPossibility(int[] nums) {
        int count=0;
        for(int i=1;i<nums.length;i++){
            if(nums[i-1]>nums[i]){
                count++;
                if(i-1<=0 ||i+1>=nums.length){

                }else if(nums[i-1]<=nums[i+1]){

                }else{
                    count++;
                }
            }
            if(count>1){
                break;
            }
        }
        if(count>1){
            return false;
        }else{
            return true;
        }

    }
}
