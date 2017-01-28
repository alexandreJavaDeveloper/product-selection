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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.sky.controller.ProductSelectionController;
import com.sky.entity.Product;
import com.sky.exception.CustomerNotFoundException;
import com.sky.exception.InvalidLocationException;
import com.sky.repository.CustomerRepository;
import com.sky.repository.ProductRepository;

@RunWith(SpringRunner.class)
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

    @SuppressWarnings("unchecked")
    @Test
    public void testPageNotFound() throws IllegalArgumentException, CustomerNotFoundException, InvalidLocationException
    {
        final String customerIdParam = "1";
        this.productSelectionController.getCustomerLocation(customerIdParam, this.model);

        final Map<String, Object> asMap = this.model.asMap();
        final Collection<Object> values = asMap.values();

        final Object[] array = values.toArray();

        final List<Product> availableProducts = (List<Product>) array[0];
        Assert.assertEquals(3, availableProducts.size());
        Assert.assertEquals(new Long(3), availableProducts.get(0).getId());
        Assert.assertEquals(new Long(4), availableProducts.get(1).getId());
        Assert.assertEquals(new Long(5), availableProducts.get(2).getId());

    }
}