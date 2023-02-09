package com.jiazhong.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        int i = 10;
        int j = 20;

        i = i^j;
        j = i^j;
        i = i^j;

        System.out.println("i="+i+"j="+j);

        System.out.println("---------------------------");
        int[] array = {2,3,3,4,4,5,5,6,7};
        int a = 0;
        for (int k = 0; k < array.length; k++) {
            a ^= array[k];
        }
        System.out.println("不相同的数是:" + a);

        System.out.println("---------------------------");

        Map<Integer,String> map = new HashMap();
        map.put(0,"aaa");
        map.put(1,"bbb");
        map.put(2,"ccc");
        ArrayList<Integer> key = new ArrayList<>(map.keySet());
        ArrayList<String> value = new ArrayList<>(map.values());
        System.out.println(key);
        System.out.println(value);


    }
}
