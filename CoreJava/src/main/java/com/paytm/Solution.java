package com.paytm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Program to scan a file and print a method
public class Solution {

    public static void main(String args[]){
        String path = "/backup/workspace/GeeksForGeeks/src/data/structure/array/arrayRotation/RotateArray.java";
        String method ="rotate";
       List<StringBuilder> str = scanAndReadMethod(path, method);
       Iterator<StringBuilder> itr=str.iterator();
       while(itr.hasNext()){
           System.out.println(itr.next());

       }
    }

    static List<StringBuilder> scanAndReadMethod(String fileName,String methodName){
        List<StringBuilder> lstString = new ArrayList<>();
        Stack<Character> stack = new Stack<>();
            try {
                File file = new File(fileName);
                Scanner scanner = new Scanner(file);
                String regexMethodName = " "+methodName+"\\(.+\\)\\{";
                String regexOpeningClosingBraces = "\\{|\\}";
                Pattern patternMethodName = Pattern.compile(regexMethodName);
                Pattern patternBraces= Pattern.compile(regexOpeningClosingBraces);
                while (scanner.hasNextLine()) {
                    StringBuilder inputString = new StringBuilder(scanner.nextLine());
                    Matcher matcherMethodName = patternMethodName.matcher(inputString);
                    if(matcherMethodName.find()){   //method found in file
                        while(true) {
                            Matcher matcherBraces = patternBraces.matcher(inputString);
                            while (matcherBraces.find()) {
                                if(matcherBraces.group().equals("{")){
                                    stack.push('{');
                                }else {
                                    stack.pop();
                                }
                            }
                            lstString.add(inputString);
                            if (stack.empty()) {
                                break;
                            } else {
                                inputString = new StringBuilder(scanner.nextLine());
                            }
                        }
                        break;
                    }
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        return lstString;
    }
}
