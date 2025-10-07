package com.iispl.movieticketsystem.display;

import java.util.List;

public class Display {
    
    public static void print(List<?> list) {
        if (list.isEmpty()) {
            System.out.println("No record found..!");
        } else
            list.forEach(System.out::println);
    }

    public static<T> void print(T data) {
        if (data != null)
        System.out.println(data);
    }
}
