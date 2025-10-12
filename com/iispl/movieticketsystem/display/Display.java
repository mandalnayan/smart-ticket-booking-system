package com.iispl.movieticketsystem.display;

import java.util.List;

public class Display {
    private static final String M_STARS = " *************************************| ";
    private static final String W_STARS = " #####################################| ";
    private static final String A_STARS = " $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$| ";
    
    public static void print(List<?> list) {
        if (list.isEmpty()) {
            System.out.println("No record found..!");
        } else
            list.forEach(System.out::println);
    }

    public static<T> void printMessage(T data) {
        if (data != null)
         System.out.println(M_STARS + data);
    }

    public static void printHeading(String message) {
        System.out.println("\n" + M_STARS + message + " !!" + M_STARS);
    }

    public static void printWarning(String message) {
        System.out.println("\n" + W_STARS + "WARNING: " + message + "!!" + W_STARS);
    }

    public static void printAlert(String message) {
        System.out.println("\n" + A_STARS + "Alert: " + message + A_STARS);
    }
}
