package com.witsoft.gongli.test.utils;

public class TestUtil {

    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer();
        StringBuffer append = buffer.append("2").append(",").append("3").append(",");

        System.out.println(append.substring(0, append.length()-1));
    }
}
