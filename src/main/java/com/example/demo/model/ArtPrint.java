package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class ArtPrint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "img")
    @Lob
    private String imgBase64;

    @NotNull
    @Size(min = 2,message ="NAme is required")
    private String name;

    @NotNull
    @ManyToOne
    @JsonIgnore
    private Category category;

    private String description;


    @OneToMany(mappedBy = "artPrint")
    private List<AP_Size> sizes = new ArrayList<>();

    private int likes;

    @JsonIgnore
    @ManyToMany(mappedBy = "likedartprints",cascade = CascadeType.REMOVE)
    private List<User> usersLiked;


    public ArtPrint() {
    }

    public ArtPrint(Long id, String imgBase64, String name, Category category, String description,List<AP_Size> ap_sizes) {
        this.id = id;
        this.imgBase64 = imgBase64;
        this.name = name;
        this.category = category;
        this.description = description;
        this.sizes=ap_sizes;
        this.likes=0;
    }

    public List<AP_Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<AP_Size> sizes) {
        this.sizes = sizes;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public List<User> getUsersLiked() {
        return usersLiked;
    }

    public void setUsersLiked(List<User> usersLiked) {
        this.usersLiked = usersLiked;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtPrint ap = (ArtPrint) o;
        return Objects.equals(name, ap.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
