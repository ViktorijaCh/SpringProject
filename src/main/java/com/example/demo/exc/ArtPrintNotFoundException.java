package com.example.demo.exc;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ArtPrintNotFoundException extends RuntimeException{
    public ArtPrintNotFoundException(Long apid) {
        super(String.format("Book with id %d was not found!",apid));
    }
    }

