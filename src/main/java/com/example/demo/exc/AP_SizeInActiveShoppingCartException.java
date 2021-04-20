package com.example.demo.exc;

public class AP_SizeInActiveShoppingCartException extends RuntimeException {
    public AP_SizeInActiveShoppingCartException(String name, String sizee) {
        super(String.format(" art print %s so golemina %s go ima vo aktivna sc",name,sizee));
    }
}
