package com.example.demo.controller;


import com.example.demo.model.ArtPrint;
import com.example.demo.repository.ArtPrintRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping()
public class HomeController {
    private final ArtPrintRepository artPrintRepository;

    public HomeController(ArtPrintRepository artPrintRepository) {
        this.artPrintRepository = artPrintRepository;
    }

    @GetMapping
    public String indexPage() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getHomePage(HttpServletResponse res, HttpServletRequest req, Model model) {
        try {
            List<ArtPrint> mostliked = artPrintRepository.findAll(Sort.by(Sort.Direction.DESC, "likes")).subList(0, 3);
            model.addAttribute("mostLiked", mostliked);
        } finally {
            return "home";
        }
    }
}


