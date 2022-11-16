package com.tutorial.apidemo.springboot.Repositories;

import com.tutorial.apidemo.springboot.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByProductName(String productName);
}
