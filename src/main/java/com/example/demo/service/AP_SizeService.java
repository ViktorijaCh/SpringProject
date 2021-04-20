package com.example.demo.service;

import com.example.demo.model.*;

import java.util.List;

public interface AP_SizeService {
    List<AP_Size> findArtPrintsBySizeId(Long sizeId);
    List<Size> findSizesByApId(Long apid);
    List<AP_Size> findAP_SizesByArtPrint_Id(Long apid);
    void save(AP_Size as);
    void deleteAP_SizesByArtPrint_Id(Long id);
    void deleteByApIdandSizeId(Long apid,Long sizeid);
}
