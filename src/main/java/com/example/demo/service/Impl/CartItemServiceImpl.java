package com.example.demo.service.Impl;


import com.example.demo.model.AP_Size;
import com.example.demo.model.CartItem;
import com.example.demo.model.CartItemId;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.enumm.CartStatus;
import com.example.demo.repository.AP_SizeRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.service.ArtPrintService;
import com.example.demo.service.CartItemService;
import com.example.demo.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRep;
    private final ArtPrintService artPrintService;
    private final ShoppingCartService shoppingCartService;
    private final AP_SizeRepository ap_sizeRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRep, ArtPrintService artPrintService, ShoppingCartService shoppingCartService, AP_SizeRepository ap_sizeRepository) {
        this.cartItemRep = cartItemRep;
        this.artPrintService = artPrintService;
        this.shoppingCartService = shoppingCartService;
        this.ap_sizeRepository = ap_sizeRepository;
    }

    @Override
    @Transactional
    public List<AP_Size> findArtPrints_sByShoppingCartId(Long shoppingCartId) {
        ShoppingCart shoppingCart = this.shoppingCartService.findById(shoppingCartId);
        List<CartItem> cartItems = shoppingCart.getCartItems();
        List<AP_Size> ap_sizes = new ArrayList<>();
        for (CartItem cartItem : cartItems){
            ap_sizes.add(cartItem.getAp_size());
        }
        return ap_sizes;
    }

    @Override
    public CartItem findById(Long artPrintId,Long sizeId, Long shoppingCartId) {
        return this.cartItemRep.findById(new CartItemId(this.ap_sizeRepository.findAP_SizeByArtPrint_IdAndAndSize_Id(artPrintId,sizeId).getId(),shoppingCartId)).orElseThrow(()->new NoSuchElementException());

    }

//    @Override
//    public List<ShoppingCart> findShoppingCartsByArtPrintId(Long artPrintId) {
//        ArtPrint ap = artPrintService.findById(artPrintId);
//        List<CartItem> cartItems = ap.getCartItems();
//        List<ShoppingCart> shoppingCarts = new ArrayList<>();
//        for (CartItem cartItem : cartItems){
//            shoppingCarts.add(cartItem.getShoppingCart());
//        }
//        return shoppingCarts;
//    }

    @Override
    @Transactional
    public List<CartItem> findCartItemsByShoppingCartId(Long id) {
        return this.cartItemRep.findCartItemsByShoppingCartId(id);
    }

    @Override
    public float totalCost(List<CartItem> cartItems) {
        float total=0;
        for (CartItem cartItem:cartItems) {
            total+=cartItem.getQuantity()*cartItem.getAp_size().getPrice();
        }
        return total;
    }

    @Override
    public void increase(CartItemId cartItemId) {
        CartItem ci=this.cartItemRep.findById(cartItemId).orElseThrow(()->new NoSuchElementException());
        ci.setQuantity(ci.getQuantity()+1);

    }

    @Override
    @Transactional
    public int containsAPSize(Long ap, Long sid) {
        return cartItemRep.containsAPSize(ap,sid, CartStatus.CREATED);
    }

}