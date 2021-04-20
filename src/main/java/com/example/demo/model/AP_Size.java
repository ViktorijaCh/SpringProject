package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
public class AP_Size  {
    @EmbeddedId
    private AP_SizeId id;

    @ManyToOne
    @MapsId("artPrintId")
    private ArtPrint artPrint;

    @ManyToOne
    @MapsId("sizeId")
    private Size size;

    @NotNull
    @Min(value = 1)
    @Digits(fraction = 0, message = "Must be a numeric value", integer = 3)
    private double price;

    @OneToMany(mappedBy = "ap_size")
    @JsonIgnore
    private List<CartItem> cartItems ;


    public AP_Size(){}
    public AP_Size(Size size,double price){
        this.size=size;
        this.price=price;
    }
    public AP_Size(ArtPrint artPrint, Size size,double price) {
        this.artPrint = artPrint;
        this.size = size;
        this.id=new AP_SizeId(artPrint.getId(), size.getId());
        this.price=price;
    }


    public AP_SizeId getId() {
        return id;
    }

    public void setId(AP_SizeId id) {
        this.id = id;
    }

    public ArtPrint getArtPrint() {
        return artPrint;
    }

    public void setArtPrint(ArtPrint artPrint) {
        this.artPrint = artPrint;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        AP_Size that = (AP_Size) o;
        return Objects.equals(artPrint, that.artPrint) &&
                Objects.equals(size, that.size);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public int hashCode() {
        return Objects.hash(artPrint, size);
    }
}
