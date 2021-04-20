package com.example.demo.controller;

import com.example.demo.model.ArtPrint;
import com.example.demo.model.User;
import com.example.demo.service.ArtPrintService;
import com.example.demo.service.AuthService;
import com.example.demo.service.ShoppingCartService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SerachController {
    private final ArtPrintService artPrintService;
    private final AuthService authService;
    private final ShoppingCartService shoppingCartService;

    public SerachController(ArtPrintService artPrintService, AuthService authService, ShoppingCartService shoppingCartService) {
        this.artPrintService = artPrintService;
        this.authService = authService;
        this.shoppingCartService = shoppingCartService;
    }
    @GetMapping
    public String getFilteredPrints(Model model, @Param("keyword") String keyword){
        List<ArtPrint> artPrintList=artPrintService.searchByKeyword(keyword);
        Integer num=0;
        try {
            User user = this.authService.getCurrentUser();
            num = shoppingCartService.findActiveShoppingCartByUsername(user.getUsername()).getCartItems().size();
            model.addAttribute("numitems",num);
        }catch (Exception e){
        }
        model.addAttribute("prints",artPrintList);
        model.addAttribute("keyword",keyword);
        return "allprints";
    }
}
