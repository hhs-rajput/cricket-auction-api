package com.cricket.mpl.controller;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
//        int[] arr=new int[]{1,2,5,-1,7,1};
//
//        int[] pf=new int[arr.length];
//        int sum=0;
//        int index=0;
//        for(int i:arr){
//            sum=sum+arr[index];
//            pf[index]=sum;
//            index++;
//        }
//        System.out.println(Arrays.toString(pf));
//
//        int[][]q=new int[4][2];
//
//        q[0][0]=1;
//        q[0][1]=4;
//        q[1][0]=2;
//        q[1][1]=3;
//        q[2][0]=1;
//        q[2][1]=2;
//        q[3][0]=3;
//        q[3][1]=5;
//        for(int[] a:q){
//            System.out.println("Sum of elements at index from "+a[0]+" to "+a[1]+" = "+(pf[a[1]] - pf[a[0]-1]));
//        }
        int[] arr=new int[]{1,2,3,4,5,6,7};

        //1,2,3,4,5,6
        //rotate 6 times
        //6,1,2,3,4,5
        //5,6,1,2,3,4
        //4,5,6,1,2,3
        //3,4,5,6,1,2
        //2,3,4,5,6,1
        //1,2,3,4,5,6

        int left=0;
        int right=arr.length-1;

        for(int i=0;i<=(arr.length/2)-1;i++){
            int temp=arr[left];
            arr[left]=arr[right];
            arr[right]=temp;
            left++;
            right--;


        }
        System.out.println(Arrays.toString(arr));
    }
}
