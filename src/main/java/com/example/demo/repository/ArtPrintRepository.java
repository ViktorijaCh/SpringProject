package com.example.demo.repository;

import com.example.demo.model.ArtPrint;
import com.example.demo.model.Category;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtPrintRepository extends JpaRepository<ArtPrint, Long> {
    List<ArtPrint> findArtPrintsByUsersLikedContaining(User user);
    List<ArtPrint>findAllByCategory(Category category);
    //List<ArtPrint>findAllByCartItems();
    List<ArtPrint>findArtPrintsByCategory_Id(Long id);
    List<ArtPrint>findArtPrintsByNameContainingOrDescriptionContaining(String keyword1,String keyword);


    @Query(value = "select ap from ArtPrint ap where lower(ap.name) like lower(concat('%', :keyword,'%')) or lower(ap.description) like lower(concat('%', :keyword,'%') ) ")
    public List<ArtPrint> findArtPrintsByNameOrDescr(@Param("keyword") String keyword);
}
