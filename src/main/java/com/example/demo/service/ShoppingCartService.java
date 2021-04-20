package com.example.demo.service;


import com.example.demo.model.ChargeRequest;
import com.example.demo.model.ShoppingCart;

public interface ShoppingCartService {

    ShoppingCart findById(Long id);

    ShoppingCart createNewShoppingCart(String userId);
    ShoppingCart findActiveShoppingCartByUsername(String userId);

    ShoppingCart addArtPrintToShoppingCart(String userId,
                                       Long apId,Long sizeId);

    ShoppingCart removeArtPrintFromShoppingCart(String userId,
                                            Long apid,Long sizeId);

    ShoppingCart getActiveShoppingCart(String userId);

    ShoppingCart cancelActiveShoppingCart(String userId);

    ShoppingCart checkoutShoppingCart(String userId, ChargeRequest chargeRequest);

    ShoppingCart deleteArtPrintFromShoppingCart(String userId, Long apId,Long sizeId);

    void increaseArtPrintToShoppingCart(String currentUserId, Long id,Long sizeId);
}
