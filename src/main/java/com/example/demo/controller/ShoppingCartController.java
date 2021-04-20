package com.example.demo.controller;


import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.service.AuthService;
import com.example.demo.service.CartItemService;
import com.example.demo.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/shopping_cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;
    private final CartItemService cartItemService;
    @Value("${STRIPE_P_KEY}")
    private String publicKey;


    public ShoppingCartController(ShoppingCartService shoppingCartService, AuthService authService, CartItemService cartItemService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public String getShoppingCart(Model model){

        ShoppingCart shoppingCart=this.shoppingCartService.getActiveShoppingCart(authService.getCurrentUserId());
        List<CartItem> cartItems = cartItemService.findCartItemsByShoppingCartId(shoppingCart.getId());
        double total=cartItemService.totalCost(cartItems);
        model.addAttribute("numitems",cartItems.size());
        model.addAttribute("user",authService.getCurrentUserId());
        model.addAttribute("cartItems",cartItems);
        model.addAttribute("currency", "eur");
        model.addAttribute("amount", (int) (total* 100));
        model.addAttribute("stripePublicKey", this.publicKey);
        return "shoppingcart";

    }




@PostMapping("/{id}/add")
    public String addArtPrintToSC(@PathVariable Long id,@RequestParam(value = "sid") Long sid){
        this.shoppingCartService.addArtPrintToShoppingCart(authService.getCurrentUserId(),id,sid);
        return "redirect:/shopping_cart";
    }

    @PostMapping("/{id}/{sid}/remove")
    public String remove(@PathVariable Long id,@PathVariable Long sid){
        this.shoppingCartService.removeArtPrintFromShoppingCart(authService.getCurrentUserId(),id,sid);
        return "redirect:/shopping_cart";
    }

    @PostMapping("/{id}/{sid}/delete")
    public String delete(@PathVariable Long id,@PathVariable Long sid){
        this.shoppingCartService.deleteArtPrintFromShoppingCart(authService.getCurrentUserId(),id,sid);
        return "redirect:/shopping_cart";
    }

    @PostMapping("/{id}/{sid}/increase")
    public String increase(@PathVariable Long id,@PathVariable Long sid){
        this.shoppingCartService.increaseArtPrintToShoppingCart(this.authService.getCurrentUserId(),id,sid);
        return "redirect:/shopping_cart";
    }
}
