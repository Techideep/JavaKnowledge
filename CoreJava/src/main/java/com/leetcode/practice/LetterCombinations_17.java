package com.leetcode.practice;

import java.util.ArrayList;
import java.util.List;

public class LetterCombinations_17 {

    public static void main(String args[]){
       List<String> output= letterCombinations("23");
       System.out.println(output);
    }
    public static List<String> letterCombinations(String digits) {
        String [] digitToLetter = new String[] {"abc","def","ghi","jkl","mno","pqrs","tuv","wxyz"};
        List<String> results= new ArrayList<>();
        results.add("");
        for(int i=0;i<digits.length();i++){
            results = combine(digitToLetter[digits.charAt(i)-'0'-2],results);
        }
        return results;
    }

    static List<String> combine(String letters, List<String> results){
        List<String> newResults= new ArrayList<>();
        for(int i=0;i<letters.length();i++){
            for(String result: results){
                newResults.add(result+letters.charAt(i));
            }
        }
        return newResults;
    }
}
