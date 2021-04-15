package com.ericsson;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Question1 {

    public static void main (String args[]){
        Set s = new LinkedHashSet();
        s.add(1);
        s.add(2.0);
        s.add(2);
        s.add(new String("1"));
        s.add(new String("1"));
        s.add(3);
        System.out.println(s);

        List lst = new LinkedList<>();
        lst.add("1");
        lst.add(1);
        System.out.println(lst);
    }
}
