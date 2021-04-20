package com.example.demo.exc;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CreatedShoppingCartExeption extends RuntimeException {
    public CreatedShoppingCartExeption(){
        super(String.format("Ima kreirano cart"));
    }
}