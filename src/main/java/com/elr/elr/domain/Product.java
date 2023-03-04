package com.elr.elr.domain;

import jakarta.persistence.*;

@Entity
@AttributeOverrides({
        @AttributeOverride(
                name = "createdDate",
                column = @Column(name = "createdDate")
        )
})

/*
* Para cambiar el nombre de la columna -> @Column(name = "createdDate")->
* 1.sed de borrar el registro dela tabla flyway que contiene el historico
* 2 modificar el script sql del archivo flyway que contiene el nombre de la tabla
* 3 Cambiar el  nombre de la tabla en ("tabla_") en column = @Column(name = "tabla_createdDate")
 * */
public class Product extends BaseEntity {
    private String description;
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        if (!super.equals(o)) return false;

        if (getDescription() != null ? !getDescription().equals(product.getDescription()) : product.getDescription() != null)
            return false;
        return getProductStatus() == product.getProductStatus();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getProductStatus() != null ? getProductStatus().hashCode() : 0);
        return result;
    }
}
