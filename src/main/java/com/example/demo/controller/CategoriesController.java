package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ShoppingCartService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoriesController {
    private final CategoryService categoryService;
    private final AuthService authService;
    private final ShoppingCartService shoppingCartService;


    public CategoriesController(CategoryService categoryService, AuthService authService, ShoppingCartService shoppingCartService) {
        this.categoryService = categoryService;
        this.authService = authService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    String categoriesPage(Model model){
        List<Category> categories = this.categoryService.findAll();
        Integer num=0;
        try {
            User user = this.authService.getCurrentUser();
            ShoppingCart shoppingCart= shoppingCartService.findActiveShoppingCartByUsername(user.getUsername());
            model.addAttribute("numitems",shoppingCart.getCartItems().size());
        }catch (Exception e){
        }
        model.addAttribute("categories",categories);
        return "categories";
    }
    @GetMapping("/new")
    @Secured("ROLE_ADMIN")
    String addCatPage(Model model){
        model.addAttribute("category",new Category());
        return "addcat";
    }
    @PostMapping
    @Secured("ROLE_ADMIN")
    String saveCat(@Valid Category category,
                   BindingResult bindingResult,Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", new Category());
            return "addcat";
        }
        if (categoryService.findByName(category.getName())==null)
            this.categoryService.save(category);
        return "redirect:/categories/new";
    }

}
