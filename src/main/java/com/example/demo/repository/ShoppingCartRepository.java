package com.example.demo.repository;

import com.example.demo.model.ShoppingCart;
import com.example.demo.model.enumm.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    List<ShoppingCart> findAllByUserUsername(String username);
    ShoppingCart findByUserUsernameAndCartStatus(String username, CartStatus status);

    boolean existsByUserUsernameAndCartStatus(String userId, CartStatus created);
}
