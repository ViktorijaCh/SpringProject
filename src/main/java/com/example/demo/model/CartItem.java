package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@ToString(exclude = "invoice")
@EqualsAndHashCode(exclude = "invoice")
@Table(name = "cart_items")
public class CartItem {

    @EmbeddedId
    CartItemId id;

    @ManyToOne
    @MapsId("shoppingCartId")
    @JsonIgnore
    ShoppingCart shoppingCart;

    @ManyToOne
    @MapsId("ap_sizeId")
    @JsonIgnore
    AP_Size ap_size;

    @Column(name = "quantity")
    private Integer quantity;

    public CartItem(){}

    public CartItem(AP_Size ap_size,ShoppingCart shoppingCart){
        this.ap_size=ap_size;
        this.shoppingCart=shoppingCart;
        this.setId(new CartItemId(ap_size.getId(),shoppingCart.getId()));
    }

    public CartItemId getId() {
        return id;
    }

    public void setId(CartItemId id) {
        this.id = id;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public AP_Size getAp_size() {
        return ap_size;
    }

    public void setAp_size(AP_Size ap_size) {
        this.ap_size = ap_size;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CartItem that = (CartItem) o;
        return Objects.equals(ap_size, that.ap_size) &&
                Objects.equals(shoppingCart, that.shoppingCart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ap_size, shoppingCart);
    }
}
