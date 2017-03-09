package com.sky;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.sky.controller.ProductSelectionController;
import com.sky.entity.Product;
import com.sky.exception.CustomerNotFoundException;
import com.sky.exception.InvalidLocationException;
import com.sky.exception.ProductNotfoundException;
import com.sky.model.Location;
import com.sky.repository.CustomerRepository;
import com.sky.repository.ProductRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ProducSelectionControllerTest
{
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    private Model model;

    private ProductSelectionController productSelectionController;

    @Before
    public void setup()
    {
        this.model = new ExtendedModelMap();
        this.productSelectionController = new ProductSelectionController(this.customerRepository, this.productRepository);
    }

    @Test
    public void testValidCustomerId() throws IllegalArgumentException, CustomerNotFoundException, InvalidLocationException
    {
        Long customerId = new Long(1);
        this.productSelectionController.getAvailableProducts(customerId, this.model);

        Set<Product> availableProducts = this.getAvailableProducts();

        Assert.assertEquals(3, availableProducts.size());
        Assert.assertTrue(availableProducts.contains(this.productRepository.findOne(new Long(3))));
        Assert.assertTrue(availableProducts.contains(this.productRepository.findOne(new Long(4))));
        Assert.assertTrue(availableProducts.contains(this.productRepository.findOne(new Long(5))));

        customerId = new Long(3);
        this.productSelectionController.getAvailableProducts(customerId, this.model);
        availableProducts = this.getAvailableProducts();

        Assert.assertEquals(4, availableProducts.size());
        Assert.assertTrue(availableProducts.contains(this.productRepository.findOne(new Long(1))));
        Assert.assertTrue(availableProducts.contains(this.productRepository.findOne(new Long(2))));
        Assert.assertTrue(availableProducts.contains(this.productRepository.findOne(new Long(4))));
        Assert.assertTrue(availableProducts.contains(this.productRepository.findOne(new Long(5))));
    }

    @SuppressWarnings("unchecked")
    private Set<Product> getAvailableProducts() throws IllegalArgumentException
    {
        final Map<String, Object> asMap = this.model.asMap();
        final Collection<Object> values = asMap.values();
        return (Set<Product>) values.toArray()[0];
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testInvalidCustomerId2() throws IllegalArgumentException, CustomerNotFoundException, InvalidLocationException
    {
        final Long customerId = new Long(1000);
        this.productSelectionController.getAvailableProducts(customerId, this.model);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testInvalidCustomerId3() throws IllegalArgumentException, CustomerNotFoundException, InvalidLocationException
    {
        final Long customerId = new Long(0);
        this.productSelectionController.getAvailableProducts(customerId, this.model);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConfirmationPage() throws IllegalArgumentException, ProductNotfoundException, CustomerNotFoundException, InvalidLocationException
    {
        final Long customerId = new Long(1);
        final Long[] products = { new Long(3) };

        // TODO simular que já está no cache no productSelection os products, e enviar tb inválido que não possua..

        // simulate the use of getAvailableProducts
        this.productSelectionController.getAvailableProducts(customerId, this.model);

        this.productSelectionController.confirmationPage(customerId, products, this.model);
        ArrayList<Product> productsToConfirm = (ArrayList<Product>) this.model.asMap().get("products");
        Assert.assertEquals(1, productsToConfirm.size());
        Assert.assertEquals(Location.LIVERPOOL, productsToConfirm.get(0).getLocation());
        Assert.assertEquals(new Long(3), productsToConfirm.get(0).getId());
        Assert.assertEquals("Liverpool TV", productsToConfirm.get(0).toString());

        final Long[] products2 = { new Long(3), new Long(4) };

        this.productSelectionController.confirmationPage(customerId, products2, this.model);
        productsToConfirm = (ArrayList<Product>) this.model.asMap().get("products");
        Assert.assertEquals(2, productsToConfirm.size());
        Assert.assertEquals(Location.LIVERPOOL, productsToConfirm.get(0).getLocation());
        Assert.assertEquals(new Long(3), productsToConfirm.get(0).getId());
        Assert.assertEquals("Liverpool TV", productsToConfirm.get(0).toString());

        Assert.assertNull(productsToConfirm.get(1).getLocation());
        Assert.assertEquals(new Long("4"), productsToConfirm.get(1).getId());
        Assert.assertEquals("Sky News", productsToConfirm.get(1).toString());
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidConfirmationPage() throws ProductNotfoundException
    {
        final Long customerId = new Long(1);
        final Long[] products = null;

        this.productSelectionController.confirmationPage(customerId, products, this.model);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInvalidConfirmationPageByWithoutPermissionFromProducts() throws IllegalArgumentException, CustomerNotFoundException,
        InvalidLocationException, ProductNotfoundException
    {
        final Long customerId = new Long(1); // customerId from Liverpool location
        final Long[] products = { new Long(4), new Long(1) }; // products from without location (ok) and from London (have not permission)

        this.productSelectionController.getAvailableProducts(customerId, this.model);

        this.productSelectionController.confirmationPage(customerId, products, this.model);

        final ArrayList<Product> productsToConfirm = (ArrayList<Product>) this.model.asMap().get("products");
        Assert.assertEquals(1, productsToConfirm.size());
        Assert.assertEquals(4, productsToConfirm.get(0).getId().intValue());
    }

    @Test
    public void testBasicsRoutes()
    {
        Assert.assertEquals("index", this.productSelectionController.index());
        Assert.assertEquals("index", this.productSelectionController.finalizeSelection());
    }
}