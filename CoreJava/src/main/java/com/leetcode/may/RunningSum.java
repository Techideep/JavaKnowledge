package com.leetcode.may;

public class RunningSum {
    public static void main(String args[]){
        int nums[]={0,1,2,3,12};
        runningSum(nums);
        for(int i=0;i<nums.length;i++){
            System.out.println(nums[i]);
        }

    }

    public static int[] runningSum(int[] nums) {
        if(nums.length<=1){
            return nums ;
        }
        for(int i=1; i<nums.length;i++){
            nums[i]=nums[i]+nums[i-1];
        }
        return nums;
    }
}
