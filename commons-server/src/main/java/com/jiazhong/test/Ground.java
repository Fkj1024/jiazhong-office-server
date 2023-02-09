package com.jiazhong.test;

public class Ground extends Thread{
    public static void main(String argv[]){
        Ground b = new Ground();
        b.run();
    }
    public void start(){
        for(int i=0;i<10;i++){
            System.out.println("Value of i = "+i);
        }
    }
}