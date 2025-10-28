package com.example.purchase.purchaseorder;

public class InvalidStatusException extends Exception{
    public InvalidStatusException(String Message) {
        super(Message);
    }
}
