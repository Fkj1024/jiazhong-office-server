package com.jiazhong.test;

public class Demo {
    static int count1 = 0;
    private  static Demo demo = new Demo();
    static  int count2 = 1;
    static int count3 = 2;
     static {
         count3++;
     }
    private  Demo(){
         count1++;
         count2++;
         ++count3;
    }
    static Demo getInstance(){
         return demo;
    }

    public static void main(String[] args) {
        Demo demo = Demo.getInstance();
        System.out.println("count1="+demo.count1);
        System.out.println("count2="+demo.count2);
        System.out.println("count3="+demo.count3);
    }

}
