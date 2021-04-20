package com.example.demo.repository;

import com.example.demo.model.ArtPrint;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends JpaRepository<User,String> {
    public List<User> findUsersByLikedartprintsIsContaining(ArtPrint artPrint);
}
