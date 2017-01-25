package com.sky;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sky.entity.Product;
import com.sky.enums.Location;
import com.sky.exception.InvalidLocationException;
import com.sky.repository.ProductRepository;
import com.sky.service.CatalogueService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
public class CatalogueServiceTest
{
    @Autowired
    private ProductRepository productRepository;

    private CatalogueService catalogueService;

    @Before
    public void setup()
    {
        this.catalogueService = new CatalogueService(this.productRepository);
    }

    @Test(expected = InvalidLocationException.class)
    public void testInvalidParam() throws InvalidLocationException
    {
        final int locationId = 0;
        this.catalogueService.getAvailableProducts(locationId);
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParam2() throws IllegalArgumentException, InvalidLocationException
    {
        final int locationId = Integer.MAX_VALUE;
        this.catalogueService.getAvailableProducts(locationId);
        Assert.fail();

    }

    @Test
    public void testValidParam() throws InvalidLocationException
    {
        int locationId = Location.LONDON.getLocationId();
        List<Product> availableProducts = this.catalogueService.getAvailableProducts(locationId);

        Collections.sort(availableProducts, (p1, p2) -> p1.compareTo(p2));

        Assert.assertEquals(4, availableProducts.size());

        Product product = availableProducts.get(0);
        Assert.assertEquals(1, product.getId().intValue());
        Assert.assertEquals(product.getLocation(), Location.LONDON);

        product = availableProducts.get(1);
        Assert.assertEquals(2, product.getId().intValue());
        Assert.assertEquals(product.getLocation(), Location.LONDON);

        product = availableProducts.get(2);
        Assert.assertEquals(4, product.getId().intValue());
        Assert.assertEquals(product.getLocation(), Location.LIVERPOOL);

        product = availableProducts.get(3);
        Assert.assertEquals(5, product.getId().intValue());
        Assert.assertNull(product.getLocation());

        locationId = Location.LIVERPOOL.getLocationId();
        availableProducts = this.catalogueService.getAvailableProducts(locationId);
        Assert.assertEquals(3, availableProducts.size());

    }
}