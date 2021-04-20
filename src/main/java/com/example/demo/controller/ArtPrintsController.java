package com.example.demo.controller;

import com.example.demo.exc.AP_SizeInActiveShoppingCartException;
import com.example.demo.model.*;
import com.example.demo.service.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/prints")
public class ArtPrintsController {
    private final ArtPrintService artPrintService;
    private final CategoryService categoryService;
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;
    private final AP_SizeService ap_sizeService;
    private final SizeService sizeService;
    private final UserService userService;
    private final CartItemService cartItemService;


    public ArtPrintsController(ArtPrintService artPrintService, CategoryService categoryService, ShoppingCartService shoppingCartService, AuthService authService, AP_SizeService ap_sizeService, SizeService sizeService, UserService userService, CartItemService cartItemService) {
        this.artPrintService = artPrintService;
        this.categoryService = categoryService;

        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
        this.ap_sizeService = ap_sizeService;
        this.sizeService = sizeService;
        this.userService = userService;

        this.cartItemService = cartItemService;
    }

    @GetMapping
    public String allPrints(Model model){
        List<ArtPrint> artPrintList=this.artPrintService.findAll();
        int num=0;


        try {
            User user = this.authService.getCurrentUser();
            model.addAttribute("user",user);
            num = shoppingCartService.findActiveShoppingCartByUsername(user.getUsername()).getCartItems().size();
            model.addAttribute("numitems",num);
        }catch (Exception e){
            model.addAttribute("user",null);
        }
        model.addAttribute("prints",artPrintList);
        return "allprints";
    }


    @GetMapping("/{id}")
    public String getBooksPage(Model model, @PathVariable Long id) {
        List<ArtPrint> prints = this.artPrintService.findAllByCategoryId(id);
        int num=0;
        try {
            User user = this.authService.getCurrentUser();
            num = shoppingCartService.findActiveShoppingCartByUsername(user.getUsername()).getCartItems().size();
            model.addAttribute("numitems",num);
        }catch (Exception e){
        }
        model.addAttribute("prints", prints);
        return "allprints";
    }
    @GetMapping("/{id}/details")
    public String getDetailsPage(Model model, @PathVariable Long id) {
        ArtPrint artPrint = this.artPrintService.findById(id);
        int num=0;
        try {
            User user = this.authService.getCurrentUser();
            num = shoppingCartService.findActiveShoppingCartByUsername(user.getUsername()).getCartItems().size();
            model.addAttribute("numitems",num);
        }catch (Exception e){
        }
        List<AP_Size>ap_sizes=ap_sizeService.findAP_SizesByArtPrint_Id(id);
        model.addAttribute("ap_sizes",ap_sizes);
        model.addAttribute("print", artPrint);
        return "artprintdetails";
    }

    @PostMapping("/{id}/like")
    public String like(Model model, @PathVariable Long id) {
        this.userService.like( id, this.authService.getCurrentUser());
        ShoppingCart shoppingCart=this.shoppingCartService.findActiveShoppingCartByUsername(this.authService.getCurrentUserId());
        int num=0;
        try {
            num = shoppingCart.getCartItems().size();
            model.addAttribute("numitems",num);
        }catch (Exception e){
        }
        return "redirect:/prints";
    }

    @PostMapping("/{id}/unlike")
    public String unlike(@PathVariable Long id) {
        User u=this.authService.getCurrentUser();
        List<User> usersLikedAP=this.artPrintService.findById(id).getUsersLiked();
//        if(usersLikedAP.contains(u)){
//            System.out.println("yes");
//        }
        this.artPrintService.unlike(id,this.authService.getCurrentUserId());
        return "redirect:/prints";
    }
    @GetMapping({"/new"})
    @Secured("ROLE_ADMIN")
    public String getAddPrintPage(Model model){
        List<Category> categories = this.categoryService.findAll();
        List<Size> sizes= this.sizeService.findAll();
        int num=0;
        try {
            User user = this.authService.getCurrentUser();
            num = shoppingCartService.findActiveShoppingCartByUsername(user.getUsername()).getCartItems().size();

        }catch (Exception e){
        }
        model.addAttribute("sizess",sizes);
        model.addAttribute("numitems",num);
        model.addAttribute("categories", categories);
        model.addAttribute("artPrint", new ArtPrint());
//        Author[] authors = new Author[10];
//        for (int i = 0; i < 10; i++) {
//            authors[i] = new Author();
//        }
//        model.addAttribute("authors", authors);
        return "addPrint";
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public String saveAP(@Valid ArtPrint artPrint,
                           BindingResult bindingResult,
                           @RequestParam(required = true) MultipartFile image,
                           Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            List<Size> sizes= this.sizeService.findAll();
            List<Category> categories = this.categoryService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("sizess",sizes);
            return "addPrint";
        }
        Integer likes=artPrint.getLikes();
        List <AP_Size> sizes = artPrint.getSizes();
        List<AP_Size> newList=new ArrayList<>();
        List<Size>delete=new ArrayList<>();
        for (AP_Size as:sizes) {
            if (as.getSize().getId()==null)
                continue;
            Size s =sizeService.findById(as.getSize().getId());
            as.setSize(s);
            newList.add(as);
        }
        List<Size>sizeList=newList.stream().map(AP_Size::getSize).collect(Collectors.toList());
        delete=sizeService.findAll().stream().filter(siz -> !sizeList.contains(siz)).collect(Collectors.toList());
        for (Size as: delete){
            if (cartItemService.containsAPSize(artPrint.getId(),as.getId())>0){
                try {
                    throw new AP_SizeInActiveShoppingCartException(artPrint.getName(),as.getSizee());
                } catch (AP_SizeInActiveShoppingCartException e) {
                    return "redirect:/prints/"+artPrint.getId()+"/edit?error=" + e.getMessage();
                }
            }
            ap_sizeService.deleteByApIdandSizeId(artPrint.getId(),as.getId());
        }
        artPrint.setSizes(newList);
        this.artPrintService.saveArtPrint(artPrint,image);
        artPrint.setLikes(likes);
        for (AP_Size as:artPrint.getSizes()) {
            as.setId(new AP_SizeId(artPrint.getId(), as.getSize().getId()));
            as.setArtPrint(artPrint);
//           as.getSize().getArtprints().add(as);
            this.ap_sizeService.save(as);

        }


        return "redirect:/categories";
    }

    @PostMapping("/{id}/delete")
    @Secured("ROLE_ADMIN")
    public String deleteAP(@PathVariable Long id) {
        List<User>users=this.artPrintService.findById(id).getUsersLiked();
        //sos cascade?????
        for (User u:users) {
            u.getLikedartprints().clear();
        }
        this.artPrintService.findById(id).getUsersLiked().clear();
        this.artPrintService.deleteById(id);
        return "redirect:/prints";
    }

    @GetMapping("/{id}/edit")
    @Secured("ROLE_ADMIN")
    public String editAPPage(Model model, @PathVariable Long id) {
        try {
            ArtPrint artPrint = this.artPrintService.findById(id);
            List<Category> categories = this.categoryService.findAll();
            List<Size> sizes= this.sizeService.findAll();
            int num=0;
            try {
                User user = this.authService.getCurrentUser();
                num = shoppingCartService.findActiveShoppingCartByUsername(user.getUsername()).getCartItems().size();

            } catch (Exception e) {

            }
            model.addAttribute("sizess",sizes);
            model.addAttribute("numitems",num);
            model.addAttribute("categories", categories);
            model.addAttribute("artPrint", artPrint);
            return "addPrint";
        } catch (RuntimeException ex) {
            return "redirect:/prints?error=" + ex.getMessage();
        }
    }


}
