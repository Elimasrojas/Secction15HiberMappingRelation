package com.elr.elr.repositories;

import com.elr.elr.domain.Product;
import com.elr.elr.domain.ProductStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;
    @Test
    void testGetCategory() {
        Product product = productRepository.findByDescription("PRODUCT1");

        assertNotNull(product);
        assertNotNull(product.getCategories());

    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setDescription("nuevo producto");
        product.setProductStatus(ProductStatus.NEW);

        Product saveProduct = productRepository.save(product);
        Product fetchedProduct= productRepository.getReferenceById(saveProduct.getId());
        assertNotNull(fetchedProduct);
        assertNotNull(fetchedProduct.getDescription());
        assertNotNull(fetchedProduct.getCreatedDate());
        assertNotNull(fetchedProduct.getLastModifiedDate());
    }

}