package com.expedia.MininumSwap;


import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

//Minimum swaps required to sort an array in Java


class Result {

    /*
     * Complete the 'minimumSwaps' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts INTEGER_ARRAY popularity as parameter.
     */

    public static int minimumSwaps(List<Integer> popularity) {
        // Write your code here
        Integer[] newPopularity = popularity.toArray(new Integer[0]);
        int length = newPopularity.length;
        int miniSwap=0;
        for(int i=0;i<length;i++){
            if(newPopularity[i]==length-i){
                continue;
            }else{
                swap(newPopularity,i,length- newPopularity[i]);
                miniSwap++;
            }
        }
        System.out.print(miniSwap);
        return miniSwap;
    }
    static void swap(Integer[] arr,int i,int j){
        int temp = arr[i];
        arr[i]=arr[j];
        arr[j]=temp;
    }

}


public class Question1 {


    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int popularityCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> popularity = IntStream.range(0, popularityCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        })
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(toList());

        int result = Result.minimumSwaps(popularity);

   //     bufferedWriter.write(String.valueOf(result));
   //     bufferedWriter.newLine();

        bufferedReader.close();
     //   bufferedWriter.close();
    }
}
