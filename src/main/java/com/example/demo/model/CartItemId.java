package com.example.demo.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartItemId implements Serializable {
    @Column(name = "artprint_id")
    private AP_SizeId ap_sizeId;

    @Column(name = "shoppindCart_id")
    private Long shoppingCartId;

    public CartItemId() {
    }

    public CartItemId(AP_SizeId ap_sizeId, Long shoppingCartId) {
        this.ap_sizeId = ap_sizeId;
        this.shoppingCartId = shoppingCartId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CartItemId that = (CartItemId) o;
        return Objects.equals(ap_sizeId, that.ap_sizeId) &&
                Objects.equals(shoppingCartId, that.shoppingCartId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ap_sizeId, shoppingCartId);
    }
}

