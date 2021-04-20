package com.example.demo.service.Impl;

import com.example.demo.model.Size;
import com.example.demo.repository.SizeRepository;
import com.example.demo.service.SizeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;

    public SizeServiceImpl(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    @Override
    public Size findBySizee(String s) {
        return sizeRepository.findSizeBySizee(s);
    }

    @Override
    public List<Size> findAll() {
        return this.sizeRepository.findAll();
    }

    @Override
    public Size findById(Long id) {
        return this.sizeRepository.findById(id).orElseThrow(()->new NoSuchElementException());
    }

    @Override
    public Size save(Size size) {
        return this.sizeRepository.save(size);
    }

    @Override
    public Size update(long id, Size size) {
        Size s=findById(id);
        s.setSizee(size.getSizee());
        return this.sizeRepository.save(s);
    }


    @Override
    public void deleteById(Long id) {
        sizeRepository.deleteById(id);
    }
}
