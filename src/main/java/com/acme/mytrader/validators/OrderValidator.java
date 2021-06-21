package com.acme.mytrader.validators;

import com.acme.mytrader.dto.Order;
import com.acme.mytrader.exceptions.OrderMismatchException;

public class OrderValidator {

    public static void validateOrder(Order customerOrder) {

        if(customerOrder.getPrice() < 0){
            throwException("BUY Order price can't be negative...");
        }
        else if(customerOrder.getSecurity()==null){
            throwException("Order Security can't be null...");
        }
        else if(customerOrder.getVolume() < 0){
            throwException("Order volume can't be negative..");
        }
    }

    public  static void throwException(String message){

        throw new IllegalArgumentException(message);

    }

}
