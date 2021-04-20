package com.example.demo.exc;

public class ShoppingCartIsNotActiveException extends RuntimeException {
    public ShoppingCartIsNotActiveException(String userId) {
        super(String.format("Ne e ni aktivna"));
    }
}