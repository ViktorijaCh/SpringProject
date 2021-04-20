package com.example.demo.repository;

import com.example.demo.model.*;
import com.example.demo.model.enumm.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {


    Optional<CartItem> findById(CartItemId id);




    @Modifying
    @Query("delete from CartItem ci where ci.ap_size.id.artPrintId=:artPrintId and ci.ap_size.id.sizeId=:sizeId and ci.shoppingCart.id=:shoppingCartId")
    void deleteCartItem(@Param("artPrintId") Long artPrintId,@Param("sizeId")Long sizeId, @Param("shoppingCartId")Long shoppingCartId);

    @Modifying
    @Query(value = "select ci from CartItem ci where ci.ap_size.id.artPrintId=:artPrintId")
    List<CartItem> findCartItemsByArtPrintId(@Param("artPrintId") Long artPrintId);

    @Modifying
    @Query(value = "select ci from CartItem ci where ci.ap_size.id.artPrintId=:artPrintId and ci.ap_size.id.sizeId=:sizeId and ci.shoppingCart.id=:shoppingCartId")
    CartItem findCartItemByAp_sizeIdSCId(@Param("artPrintId") Long artPrintId,@Param("sizeId")Long sizeId, @Param("shoppingCartId")Long shoppingCartId);

    List<CartItem> findCartItemsByShoppingCartId(Long id);

    //@Modifying
    @Query(value = "select count(ci) from CartItem ci where ci.ap_size.id.artPrintId=:artPrintId and ci.ap_size.id.sizeId=:sizeId and ci.shoppingCart.cartStatus=:status")
    int containsAPSize(@Param("artPrintId") Long artPrintId, @Param("sizeId") Long sizeId,@Param("status") CartStatus status);


}
