package com.elr.elr.repositories;

import com.elr.elr.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //Product findByDescription(String description);
    Optional<Product> findByDescription(String description);
}