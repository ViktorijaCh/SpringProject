package com.example.demo.controller;


import com.example.demo.model.CartItem;
import com.example.demo.model.ChargeRequest;
import com.example.demo.model.ShoppingCart;
import com.example.demo.service.AuthService;
import com.example.demo.service.CartItemService;
import com.example.demo.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/payments")
public class PaymentController {
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;
    @Value("${STRIPE_P_KEY}")
    private String publicKey;
    private final CartItemService cartItemService;
   // private final TransactionRepository transactionRepository;

    public PaymentController(ShoppingCartService shoppingCartService, AuthService authService, CartItemService cartItemService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
        this.cartItemService = cartItemService;
      //  this.transactionRepository = transactionRepository;
    }
    @GetMapping("/charge")
    public String getShoppingCart(Model model){

        ShoppingCart shoppingCart=this.shoppingCartService.getActiveShoppingCart(authService.getCurrentUserId());
        List<CartItem> cartItems = cartItemService.findCartItemsByShoppingCartId(shoppingCart.getId());
        double total=cartItemService.totalCost(cartItems);
        model.addAttribute("cartItems",cartItems);
        model.addAttribute("currency", "eur");
        model.addAttribute("amount", (int) (total* 100));
        model.addAttribute("stripePublicKey", this.publicKey);
        return "shoppingcart";

    }

    @PostMapping("/charge")
    public String checkout(ChargeRequest chargeRequest, Model model) {
        try {
            ShoppingCart shoppingCart = this.shoppingCartService.checkoutShoppingCart(this.authService.getCurrentUserId(), chargeRequest);
//            Transaction transaction=new Transaction();
//            transaction.setAmount(chargeRequest.getAmount()/100);
//            transaction.setCurrency(chargeRequest.getCurrency());
//            transaction.setDescription(chargeRequest.getDescription());
//            transaction.setSource(chargeRequest.getStripeToken());
//            transaction.setUsername(this.authService.getCurrentUser());
//            this.transactionRepository.save(transaction);
            return "redirect:/prints?message=Successful Payment";
        } catch (RuntimeException ex) {
            return "redirect:/shoppingcart/?error=" + ex.getLocalizedMessage();
        }
    }
}
