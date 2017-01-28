package com.sky;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import com.sky.repository.CustomerRepository;
import com.sky.repository.ProductRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
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
        String customerId = "1";
        List<Product> availableProducts = this.getAvailableProducts(customerId);

        Assert.assertEquals(3, availableProducts.size());
        Assert.assertEquals(new Long(3), availableProducts.get(0).getId());
        Assert.assertEquals(new Long(4), availableProducts.get(1).getId());
        Assert.assertEquals(new Long(5), availableProducts.get(2).getId());

        customerId = "3";
        availableProducts = this.getAvailableProducts(customerId);

        Assert.assertEquals(4, availableProducts.size());
        Assert.assertEquals(new Long(1), availableProducts.get(0).getId());
        Assert.assertEquals(new Long(2), availableProducts.get(1).getId());
        Assert.assertEquals(new Long(4), availableProducts.get(2).getId());
        Assert.assertEquals(new Long(5), availableProducts.get(3).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCustomerId() throws IllegalArgumentException, CustomerNotFoundException, InvalidLocationException
    {
        final String customerId = "3sfggfdgd";
        this.productSelectionController.getCustomerLocation(customerId, this.model);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testInvalidCustomerId2() throws IllegalArgumentException, CustomerNotFoundException, InvalidLocationException
    {
        final String customerId = "1000";
        this.productSelectionController.getCustomerLocation(customerId, this.model);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testInvalidCustomerId3() throws IllegalArgumentException, CustomerNotFoundException, InvalidLocationException
    {
        final String customerId = "0";
        this.productSelectionController.getCustomerLocation(customerId, this.model);
    }

    @SuppressWarnings("unchecked")
    private List<Product> getAvailableProducts(final String customerId) throws IllegalArgumentException, CustomerNotFoundException, InvalidLocationException
    {
        this.productSelectionController.getCustomerLocation(customerId, this.model);
        final Map<String, Object> asMap = this.model.asMap();
        final Collection<Object> values = asMap.values();
        return (List<Product>) values.toArray()[0];
    }
}