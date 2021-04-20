package com.example.demo.service.Impl;

import com.example.demo.model.*;
import com.example.demo.repository.AP_SizeRepository;
import com.example.demo.service.AP_SizeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AP_SizeServiceImpl implements AP_SizeService {
    private final AP_SizeRepository ap_sizeRepository;

    public AP_SizeServiceImpl(AP_SizeRepository ap_sizeRepository) {
        this.ap_sizeRepository = ap_sizeRepository;
    }

    @Override
    public List<AP_Size> findArtPrintsBySizeId(Long sizeId) {
        return this.ap_sizeRepository.findAP_SizesBySize_Id(sizeId);
    }

    @Override
    public List<Size> findSizesByApId(Long apid) {
        List<AP_Size> ap_sizes=ap_sizeRepository.findAP_SizesByArtPrint_Id(apid);
        List<Size>sizes = new ArrayList<>();
        for (AP_Size as:ap_sizes) {
            sizes.add(as.getSize());
        }
        return sizes;
    }

    @Override
    public List<AP_Size> findAP_SizesByArtPrint_Id(Long apid) {
        return this.ap_sizeRepository.findAP_SizesByArtPrint_Id(apid);
    }

    @Override
    public void save(AP_Size as) {
        this.ap_sizeRepository.save(as);
    }

    @Override
    @Transactional
    public void deleteAP_SizesByArtPrint_Id(Long id) {
        this.ap_sizeRepository.deleteAP_SizesByArtPrint_Id(id);
    }

    @Override
    @Transactional
    public void deleteByApIdandSizeId(Long apid, Long sizeid) {
        this.ap_sizeRepository.deleteAP_SizesByArtPrint_IdAAndSizeId(apid,sizeid);
    }

}
