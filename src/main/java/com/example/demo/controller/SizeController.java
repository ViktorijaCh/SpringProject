package com.example.demo.controller;

import com.example.demo.model.Size;
import com.example.demo.service.SizeService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/size")
public class SizeController {
    private final SizeService sizeService;

    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }
    @GetMapping
    @Secured("ROLE_ADMIN")
    String addSize(Model model){
        model.addAttribute("size",new Size());
        return "addsize";
    }
    @PostMapping
    @Secured("ROLE_ADMIN")
    String saveSize(@Valid Size size,
                   BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("size", new Size());
            return "addsize";
        }
        if (this.sizeService.findBySizee(size.getSizee())==null)
            this.sizeService.save(size);
        return "redirect:/size";
    }
}
