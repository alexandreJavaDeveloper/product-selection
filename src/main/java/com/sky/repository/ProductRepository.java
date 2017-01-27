package com.sky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sky.entity.Product;
import com.sky.model.Location;

public interface ProductRepository extends JpaRepository<Product, Long>
{
    @Query("SELECT p FROM Product p WHERE p.location = :location OR p.location IS NULL")
    List<Product> findByLocation(@Param("location") Location location);
}