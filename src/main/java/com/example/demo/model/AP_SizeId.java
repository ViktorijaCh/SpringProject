package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AP_SizeId implements Serializable {
    @Column(name = "artPrintId")
    private Long artPrintId;

    @Column(name = "sizeId")
    private Long sizeId;

    private AP_SizeId() {}

    public AP_SizeId(Long artPrintId, Long sizeId) {
        this.artPrintId = artPrintId;
        this.sizeId = sizeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        AP_SizeId that = (AP_SizeId) o;
        return Objects.equals(artPrintId, that.artPrintId) &&
                Objects.equals(sizeId, that.sizeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artPrintId, sizeId);
    }
}
