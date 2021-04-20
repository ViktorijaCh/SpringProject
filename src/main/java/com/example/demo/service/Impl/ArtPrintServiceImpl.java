package com.example.demo.service.Impl;

import com.example.demo.exc.ArtPrintInActiveShoppingCart;
import com.example.demo.model.*;
import com.example.demo.repository.AP_SizeRepository;
import com.example.demo.repository.ArtPrintRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.ArtPrintService;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ArtPrintServiceImpl implements ArtPrintService {
    private final ArtPrintRepository artPrintRepository;
    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    private final AP_SizeRepository ap_sizeRepository;
    private final UserService userService;

    public ArtPrintServiceImpl(ArtPrintRepository artPrintRepository, CategoryRepository categoryRepository, CartItemRepository cartItemRepository, AP_SizeRepository ap_sizeRepository, UserService userService) {
        this.artPrintRepository = artPrintRepository;
        this.categoryRepository = categoryRepository;

        this.cartItemRepository = cartItemRepository;
        this.ap_sizeRepository = ap_sizeRepository;
        this.userService = userService;
    }

    @Override
    public ArtPrint findById(Long id) {
        return this.artPrintRepository.findById(id).orElseThrow(()->new NoSuchElementException());
    }

    @Override
    public List<ArtPrint> findAll() {
        return artPrintRepository.findAll();
    }

    @Override
    @Transactional
    public List<ArtPrint> findAllByCategoryId(Long category) {
        return this.artPrintRepository.findArtPrintsByCategory_Id(category);
    }

    @Override
    @Transactional
    public ArtPrint saveArtPrint(ArtPrint artPrint, MultipartFile image) throws IOException {
        Category kat = this.categoryRepository.findById(artPrint.getCategory().getId()).orElseThrow(()->new NoSuchElementException());
        artPrint.setCategory(kat);
        kat.getArtPrints().add(artPrint);
        if (image != null && !image.getName().isEmpty()) {
            byte[] bytes = image.getBytes();
            String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(bytes));
            artPrint.setImgBase64(base64Image);
        }
        ArtPrint ap= this.artPrintRepository.save(artPrint);
        return ap;
    }

//    @Override
//    public ArtPrint updateArtPrint(Long id, ArtPrint artPrint, MultipartFile image) throws IOException {
//        return null;
//    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        List<CartItem> cartItems = this.cartItemRepository.findCartItemsByArtPrintId(id);
//        int flag=0;
        if (cartItems.stream()
                .anyMatch(ci->ci.getShoppingCart().getCartStatus().toString().equals("CREATED")))
//        for (CartItem ci :cartItems){
//            if (ci.getShoppingCart().getCartStatus().toString().equals("CREATED")) {
//                flag = 1;
//                break;
//            }
//        }
//        if (flag==1){
            throw new ArtPrintInActiveShoppingCart(artPrintRepository.getOne(id).getId());
//        }
        this.ap_sizeRepository.deleteAP_SizesByArtPrint_Id(id);
        this.artPrintRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<ArtPrint> searchByKeyword(String keyword) {
        if (keyword==null)
            return this.artPrintRepository.findAll();
        return this.artPrintRepository.findArtPrintsByNameOrDescr(keyword);
//        return this.artPrintRepository.findArtPrintsByNameContainingOrDescriptionContaining(keyword,keyword);
    }

    @Override
    public void unlike(Long id,String currentUserId) {
        ArtPrint artPrint=this.findById(id);
        artPrint.setLikes(artPrint.getLikes()-1);
        List<User>newUsers=new ArrayList<>();
        List<ArtPrint>newAP=new ArrayList<>();
        User user=this.userService.findById(currentUserId);
        for (User u:artPrint.getUsersLiked()) {
            if (!u.getUsername().equals(user.getUsername()))
                newUsers.add(u);
        }
        for (ArtPrint ap:user.getLikedartprints()) {
            if (ap.getId()!=artPrint.getId())
                newAP.add(ap);
        }
        artPrint.getUsersLiked().clear();
        artPrint.setUsersLiked(newUsers);
        artPrintRepository.save(artPrint);
        user.getLikedartprints().clear();
        user.setLikedartprints(newAP);
        userService.save(user);

    }
}
