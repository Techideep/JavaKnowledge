package com.leetcode.april;

public class MoveZero {

    public static void main(String args[]){
        int nums[]={0,1,0,3,12};
        moveZeroes(nums);
        for(int i=0;i<nums.length;i++){
            System.out.println(nums[i]);
        }

    }

    public static void moveZeroes(int[] nums) {
        int j=0;
        for(int i=0;i<nums.length;i++){
            if(nums[i]==0){
                do{
                    i++;
                }while(i <nums.length && nums[i]==0);
                if(i<nums.length)
                {
                    nums[j]=nums[i];
                    j++;
                }else{
                    break;
                }
            }else{
                nums[j]=nums[i];
                j++;
            }
        }

        for( ;j<nums.length;j++){
            nums[j]=0;
        }

    }
}
