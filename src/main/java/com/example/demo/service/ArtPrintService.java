package com.example.demo.service;

import com.example.demo.model.ArtPrint;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArtPrintService {
    ArtPrint findById(Long id);
    List<ArtPrint> findAll();
    List<ArtPrint> findAllByCategoryId(Long category);
    ArtPrint saveArtPrint(ArtPrint artPrint,MultipartFile image) throws IOException;
   // ArtPrint updateArtPrint(Long id,ArtPrint artPrint,MultipartFile image) throws IOException;
    void deleteById(Long id);
    List<ArtPrint> searchByKeyword(String keyword);


    void unlike(Long id,String currentUserId);

}




//    List<Book> findAll();
//    Book findById(Long id);
//    // byte[] getImageById(Long id);
//    //Book saveBook(String ime , Long brPrimeroci,Long categoryId, MultipartFile image) throws IOException;
//    Book saveBook(Book book, MultipartFile image) throws IOException;
//    Book updateBook(Long id, Book book, MultipartFile image) throws IOException;
//    void deleteById(Long id);