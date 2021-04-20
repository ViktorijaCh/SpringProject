package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String sizee;

    @OneToMany(
            mappedBy = "size",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AP_Size> artprints = new ArrayList<>();

    public Size(){}

    public Size(Long id, @NotNull String sizee) {
        this.id = id;
        this.sizee = sizee;
    }

    public List<AP_Size> getArtprints() {
        return artprints;
    }

    public void setArtprints(List<AP_Size> artprints) {
        this.artprints = artprints;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSizee() {
        return sizee;
    }

    public void setSizee(String size) {
        this.sizee = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Size size = (Size) o;
        return Objects.equals(size, size.sizee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sizee);
    }

}
