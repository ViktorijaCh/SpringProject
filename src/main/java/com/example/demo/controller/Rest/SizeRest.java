package com.example.demo.controller.Rest;

import com.example.demo.model.AP_Size;
import com.example.demo.model.ArtPrint;
import com.example.demo.model.Size;
import com.example.demo.service.AP_SizeService;
import com.example.demo.service.SizeService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/size")
public class SizeRest {
    private final SizeService sizeService;
    private final AP_SizeService ap_sizeService;

    public SizeRest(SizeService sizeService, AP_SizeService ap_sizeService) {
        this.sizeService = sizeService;
        this.ap_sizeService = ap_sizeService;
    }
    @GetMapping
    public List<Size> findAll(@RequestParam(required = false) String ime){
        return this.sizeService.findAll();

    }
    @GetMapping("/{id}")
    public List<String> findById(@PathVariable Long id){
        Size c=this.sizeService.findById(id);
        List<AP_Size>ap_sizes=ap_sizeService.findArtPrintsBySizeId(id);
        List<String> aps=new ArrayList<>();
        for (AP_Size as:ap_sizes) {

            ArtPrint ap =as.getArtPrint();
            aps.add(ap.getName());
        }
        return aps;

    }
}
