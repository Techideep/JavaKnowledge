package com.array;

import java.io.*;
        import java.math.*;
        import java.security.*;
        import java.text.*;
        import java.util.*;
        import java.util.concurrent.*;
        import java.util.function.*;
        import java.util.regex.*;
        import java.util.stream.*;
        import static java.util.stream.Collectors.joining;
        import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Complete the 'performOperations' function below.
     *
     * The function is expected to return a LONG_INTEGER_ARRAY.
     * The function accepts following parameters:
     *  1. INTEGER N
     *  2. INTEGER_ARRAY op
     */

    public static List<Long> performOperations(int N, List<Integer> op) {
        List<Long> output = new ArrayList();
        int arr[] = new int[N];
        for(int i=0;i<N;i++){
            arr[i]=i+1;
        }
        int sum = N * (N+1) /2 ;
        Iterator iterator = op.iterator();
        int temp=0;
        while(iterator.hasNext()){
            int val=(int)iterator.next();
            if(val==0){
                if(arr[0]==val || arr[N-1]==val){
                    temp=arr[0];
                    arr[0]=arr[N-1];
                    arr[N-1]=temp;
                }else{
                    sum = sum -arr[N-1] + val;
                    arr[N-1] = val;
                }
            }else if (val>=N){
                if(arr[0]==val || arr[N-1]==val){
                    temp=arr[0];
                    arr[0]=arr[N-1];
                    arr[N-1]=temp;
                }else{
                    sum = sum -arr[N-1] + val;
                    arr[N-1] = val;
                }
            }else{
                if(arr[0]==val || arr[N-1]==val || arr[val-1]==val){
                    temp=arr[0];
                    arr[0]=arr[N-1];
                    arr[N-1]=temp;
                }else{
                    sum = sum -arr[N-1] + val;
                    arr[N-1] = val;
                }
            }
            output.add(Long.valueOf(sum));

        }

        return output;
        // Write your code here

    }

}

public class ArrayCumOperation {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      //  BufferedWriter bufferedWriter = new BufferedWriter((System.out));
        List<Integer> op = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());
        int N = op.get(0);
        int M=op.get(1);
        List<Integer> operatios = new ArrayList<>();
        for(int i=0 ;i <M;i++){
            operatios.add(Integer.parseInt(bufferedReader.readLine().trim()));
        }

        List<Long> result = Result.performOperations(N, operatios);

        for (Long val:result
             ) {
            System.out.println(val);
        }
       // bufferedWriter.write

        bufferedReader.close();
     //   bufferedWriter.close();
    }
}
