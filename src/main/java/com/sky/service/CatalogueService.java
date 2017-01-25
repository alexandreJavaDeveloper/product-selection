package com.sky.service;

import java.util.List;

import com.sky.entity.Product;
import com.sky.enums.Location;
import com.sky.exception.InvalidLocationException;
import com.sky.repository.ProductRepository;

public class CatalogueService
{
    private final ProductRepository productRepository;

    public CatalogueService(final ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    public List<Product> getAvailableProducts(final int locationId) throws InvalidLocationException, IllegalArgumentException
    {
        if (locationId <= 0)
            throw new InvalidLocationException();

        return this.productRepository.findByLocation(Location.findById(locationId));
    }
}