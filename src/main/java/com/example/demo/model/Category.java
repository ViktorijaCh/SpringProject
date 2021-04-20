package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;

import javax.persistence.*;

import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<ArtPrint> artPrints;

    public Category(){}

    public Category(Long id,String name){
        this.id=id;
        this.name=name;
    }

    public Long getId() {
        return id;
    }

    public List<ArtPrint> getArtPrints() {
        return artPrints;
    }

    public void setArtPrints(List<ArtPrint> artPrints) {
        this.artPrints = artPrints;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
