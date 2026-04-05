package com.cricket.mpl.controller;

import java.util.Arrays;

public class BinSearch {
    public static void main(String[] args) {
        int[] ar=new int[]{1,2,3,4,5,6,7,8,9,10};

        int left=0;
        int right=ar.length-1;
        int target=7;
        int mid=0;
        while(left<=right){
             mid=(left+right)/2;

            if(ar[mid]==target){
                break;
            } else if (ar[mid]<target) {
                left=mid+1;

            }else{
                right=mid-1;
            }
        }
        System.out.println(mid);
    }
}
