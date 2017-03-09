package com.sky.service;

import java.util.List;

import com.sky.entity.Product;
import com.sky.exception.InvalidLocationException;
import com.sky.exception.ProductNotfoundException;
import com.sky.model.Location;
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

    public Product findOne(final Long productId) throws ProductNotfoundException
    {
        final Product product = this.productRepository.findOne(productId);

        if (product == null)
            throw new ProductNotfoundException();

        return product;
    }
}