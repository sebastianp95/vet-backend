package com.dev.vetbackend.repository;

import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//        List<Product> findAllByUser(User user);
        Product save(Product product);
        int countByUser(User user);
}
