package com.example.demo.service;


import com.example.demo.model.Size;

import java.util.List;

public interface SizeService {
    Size findBySizee(String s);
    List<Size> findAll();
    Size findById(Long id);
    Size save(Size size);
    Size update(long id , Size size);
    void deleteById(Long id);
}
