package com.example.demo.service;

import com.example.demo.model.*;

import java.util.List;

public interface CartItemService {

    List<AP_Size> findArtPrints_sByShoppingCartId(Long shoppingCartId);

    CartItem findById(Long apId,Long sizeId, Long shoppingCartId);

    List<CartItem> findCartItemsByShoppingCartId(Long id);

    float totalCost(List<CartItem> cartItems);

    void increase(CartItemId cartItemId);

    int containsAPSize(Long id, Long id1);
}