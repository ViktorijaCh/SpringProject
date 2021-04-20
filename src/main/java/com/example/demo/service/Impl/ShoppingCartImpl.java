package com.example.demo.service.Impl;

import com.example.demo.exc.CreatedShoppingCartExeption;
import com.example.demo.exc.TransactionFailedException;
import com.example.demo.model.*;

import com.example.demo.model.enumm.CartStatus;
import com.example.demo.repository.*;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ShoppingCartService;
import com.example.demo.service.UserService;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ShoppingCartImpl implements ShoppingCartService {
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final AP_SizeRepository ap_sizeRepository;
    private final PaymentService paymentService;


    public ShoppingCartImpl(UserService userService, ShoppingCartRepository shoppingCartRepository, CartItemRepository cartItemRepository, AP_SizeRepository ap_sizeRepository, PaymentService paymentService) {
        this.userService = userService;
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
        this.ap_sizeRepository = ap_sizeRepository;

        this.paymentService = paymentService;
    }
    // private final PaymentService paymentService;



    @Override
    public ShoppingCart findActiveShoppingCartByUsername(String userId) {
        ShoppingCart sc= this.shoppingCartRepository.findByUserUsernameAndCartStatus(userId, CartStatus.CREATED);
        if (sc==null)
           sc= this.createNewShoppingCart(userId);
        return sc;

    }

    @Override
    public ShoppingCart findById(Long id) {
        return this.shoppingCartRepository.findById(id).orElseThrow(()->new NoSuchElementException());
    }

    @Override
    public ShoppingCart createNewShoppingCart(String userId) {
        if (this.shoppingCartRepository.existsByUserUsernameAndCartStatus(userId, CartStatus.CREATED)){
            throw new CreatedShoppingCartExeption();
        }
        User u = userService.findById(userId);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(u);
        return shoppingCartRepository.save(shoppingCart);

    }

    @Override
    @Transactional
    public ShoppingCart addArtPrintToShoppingCart(String userId, Long artprintId,Long sizeId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
//        ArtPrint artPrint = artPrintRepository.findById(artprintId).orElseThrow(()->new ArtPrintNotFoundException(artprintId));
        AP_Size ap_size = this.ap_sizeRepository.findAP_SizeByArtPrint_IdAndAndSize_Id(artprintId,sizeId);
        if (!this.cartItemRepository.findById(new CartItemId(new AP_SizeId(artprintId, sizeId), shoppingCart.getId())).isPresent()){
            CartItem cartItem = new CartItem(ap_size,shoppingCart);
            cartItem.setQuantity(1);
            cartItemRepository.save(cartItem);
            ShoppingCart sc1 = cartItem.getShoppingCart();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setAp_size(ap_size);
            shoppingCart.getCartItems().add(cartItem);
            ap_size.getCartItems().add(cartItem);
            ap_sizeRepository.save(ap_size);
            return shoppingCartRepository.save(shoppingCart);

        }else{
            CartItem cartItem = this.cartItemRepository.findById(new CartItemId(new AP_SizeId(artprintId, sizeId), shoppingCart.getId())).orElseThrow(()->new NoSuchElementException());
            cartItem.setQuantity(cartItem.getQuantity()+1);
            return shoppingCart;
        }
    }

    @Override
    @Transactional
    public ShoppingCart getActiveShoppingCart(String userId) {
        ShoppingCart sc= this.shoppingCartRepository
                .findByUserUsernameAndCartStatus(userId, CartStatus.CREATED);
        if (sc==null)
            return createNewShoppingCart(userId);
        return sc;
    }


    @Override
    @Transactional
    public ShoppingCart cancelActiveShoppingCart(String userId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository
                .findByUserUsernameAndCartStatus(userId, CartStatus.CREATED);
        shoppingCart.setCartStatus(CartStatus.CANCELED);
        shoppingCart.setClosedDate(LocalDateTime.now());
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart checkoutShoppingCart(String userId, ChargeRequest chargeRequest) {

        ShoppingCart shoppingCart = this.findActiveShoppingCartByUsername(userId);

        List<CartItem> cartItems = shoppingCart.getCartItems();
        float price = 0;

        for (CartItem cartItem : cartItems) {
            price += cartItem.getQuantity()*cartItem.getAp_size().getPrice();
        }

        //this.paymentService.pay(price);
        Charge charge = null;
        try {
            charge = this.paymentService.pay(chargeRequest);
        } catch (CardException | APIException | AuthenticationException | APIConnectionException | InvalidRequestException e) {
            throw new TransactionFailedException(userId, e.getLocalizedMessage());
        }


        shoppingCart.setCartItems(cartItems);
        shoppingCart.setCartStatus(CartStatus.FINISHED);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart deleteArtPrintFromShoppingCart(String userId, Long artprintId,Long sizeId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndCartStatus(userId,CartStatus.CREATED);
        AP_Size ap_size = this.ap_sizeRepository.findAP_SizeByArtPrint_IdAndAndSize_Id(artprintId,sizeId);
        CartItem cartItem = this.cartItemRepository.findById(new CartItemId(new AP_SizeId(ap_size.getArtPrint().getId(),ap_size.getSize().getId()), shoppingCart.getId())).orElseThrow(()->new NoSuchElementException());
        cartItemRepository.deleteCartItem(artprintId,sizeId,shoppingCart.getId());
        shoppingCart.setCartItems(
                shoppingCart.getCartItems()
                        .stream()
                        .filter(cartItem1 -> !cartItem1.equals(cartItem))
                        .collect(Collectors.toList())
        );
        ap_size.setCartItems(
                ap_size.getCartItems()
                        .stream()
                        .filter(cartItem1 -> !cartItem1.equals(cartItem))
                        .collect(Collectors.toList())
        );
        //booksRepository.save(artPrint);
        return shoppingCart;
    }

    @Override
    @Transactional
    public ShoppingCart removeArtPrintFromShoppingCart(String userId, Long artItemId,Long sizeId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        // ArtPrint artPrint = this.artPrintRepository.getOne(artItemId);
        AP_Size ap_size = this.ap_sizeRepository.findAP_SizeByArtPrint_IdAndAndSize_Id(artItemId,sizeId);
        CartItem cartItem1 = cartItemRepository.findById(new CartItemId(new AP_SizeId(ap_size.getArtPrint().getId(),ap_size.getSize().getId()), shoppingCart.getId())).orElseThrow(()->new NoSuchElementException());
        if (cartItem1 != null) {
            if (cartItem1.getQuantity()==1) {
                shoppingCart.setCartItems(
                        shoppingCart.getCartItems()
                                .stream()
                                .filter(cartItem -> !cartItem.equals(cartItem1))
                                .collect(Collectors.toList())
                );
                ap_size.setCartItems(
                        ap_size.getCartItems()
                                .stream()
                                .filter(cartItem -> !cartItem.equals(cartItem1))
                                .collect(Collectors.toList())
                );
                cartItemRepository.deleteCartItem(ap_size.getArtPrint().getId(),ap_size.getSize().getId(),shoppingCart.getId());
                ap_sizeRepository.save(ap_size);
                return this.shoppingCartRepository.save(shoppingCart);
            }
        }
        cartItem1.setQuantity(cartItem1.getQuantity()-1);
        cartItemRepository.save(cartItem1);
        return shoppingCart;
    }
    @Override
    @Transactional
    public void increaseArtPrintToShoppingCart(String currentUserId, Long id,Long sizeId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndCartStatus(currentUserId,CartStatus.CREATED);
        CartItem cartItem = cartItemRepository.findById(new CartItemId(new AP_SizeId(id,sizeId), shoppingCart.getId())).orElseThrow(()->new NoSuchElementException());
        Integer nov=cartItem.getQuantity()+1;
        cartItem.setQuantity(nov);

    }
}
