package com.example.demo.exc;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ArtPrintInActiveShoppingCart extends RuntimeException {
    public ArtPrintInActiveShoppingCart(Long id) {
            super(String.format("Slikata %s ja ima vo aktivna shoppingCart, ne moze da se izbrisha",id));
        }
    }

