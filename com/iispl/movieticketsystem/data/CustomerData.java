package com.iispl.movieticketsystem.data;

import java.util.ArrayList;
import java.util.List;

import com.iispl.movieticketsystem.pojos.Customer;

public class CustomerData {
    private static List<Customer> customers = new ArrayList<>();

    static {
        customers.add(new Customer("Nayan Mandal", "121212", "nayan@123"));
        customers.add(new Customer("Mohan Mandal", "9999", "mohan@123"));
        customers.add(new Customer("Ram Mandal", "8873", "ram@123"));
    }

    public static List<Customer> getCustomers() {
        return customers;
    }
}
