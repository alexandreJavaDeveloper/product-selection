package com.sky;

import java.util.ArrayList;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.sky.controller.ProductSelectionController;
import com.sky.entity.Product;
import com.sky.exception.CustomerNotFoundException;
import com.sky.exception.InvalidLocationException;
import com.sky.i18n.StringsI18N;
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
        String customerId = String.valueOf("1");
        this.productSelectionController.getCustomerLocation(customerId, this.model);
        List<Product> availableProducts = this.getAvailableProducts();

        Assert.assertEquals(3, availableProducts.size());
        Assert.assertEquals("Liverpool TV", availableProducts.get(0).toString());
        Assert.assertEquals(new Long(3), availableProducts.get(0).getId());
        Assert.assertEquals(new Long(4), availableProducts.get(1).getId());
        Assert.assertEquals(new Long(5), availableProducts.get(2).getId());

        customerId = String.valueOf("3");
        this.productSelectionController.getCustomerLocation(customerId, this.model);
        availableProducts = this.getAvailableProducts();

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
    @Test
    public void testConfirmationPage() throws IllegalArgumentException
    {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        List<Object> listProduct = new ArrayList<>();
        String productId = "3"; // product with Liverpool location
        listProduct.add(productId);
        map.put(productId, listProduct);

        this.productSelectionController.confirmationPage(map, this.model);
        ArrayList<Product> productsToConfirm = (ArrayList<Product>) this.model.asMap().get("products");
        Assert.assertEquals(1, productsToConfirm.size());
        Assert.assertEquals(Location.LIVERPOOL, productsToConfirm.get(0).getLocation());
        Assert.assertEquals(new Long(productId), productsToConfirm.get(0).getId());
        Assert.assertEquals("Liverpool TV", productsToConfirm.get(0).toString());

        map = new LinkedMultiValueMap<String, Object>();
        listProduct = new ArrayList<>();
        productId = "3"; // product with Liverpool location
        listProduct.add(productId);
        map.put(productId, listProduct);

        listProduct = new ArrayList<>();
        listProduct.add("4"); // product without Location
        map.put("4", listProduct);

        listProduct = new ArrayList<>();
        map.put("1", listProduct);
        listProduct.add("");
        listProduct = new ArrayList<>();
        map.put("2", listProduct);

        this.productSelectionController.confirmationPage(map, this.model);
        productsToConfirm = (ArrayList<Product>) this.model.asMap().get("products");
        Assert.assertEquals(2, productsToConfirm.size());
        Assert.assertEquals(Location.LIVERPOOL, productsToConfirm.get(0).getLocation());
        Assert.assertEquals(new Long(productId), productsToConfirm.get(0).getId());
        Assert.assertEquals("Liverpool TV", productsToConfirm.get(0).toString());

        Assert.assertNull(productsToConfirm.get(1).getLocation());
        Assert.assertEquals(new Long("4"), productsToConfirm.get(1).getId());
        Assert.assertEquals("Sky News", productsToConfirm.get(1).toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInvalidConfirmationPage() throws IllegalArgumentException
    {
        final MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        final List<Object> listProduct = new ArrayList<>();
        map.put("1", listProduct);

        this.productSelectionController.confirmationPage(map, this.model);
        final ArrayList<Product> productsToConfirm = (ArrayList<Product>) this.model.asMap().get("products");
        Assert.assertTrue(productsToConfirm.isEmpty());
    }

    @Test
    public void testBasicsRoutes()
    {
        Assert.assertEquals("index", this.productSelectionController.index());
        Assert.assertEquals("index", this.productSelectionController.finalizeSelection());
    }

    @Test
    public void testi18N()
    {
        Assert.assertEquals("Please, enter only numbers.", StringsI18N.PROBLEM_READING_NUMBERS);
        Assert.assertEquals("There was a problem retrieving the customer information.", StringsI18N.PROBLEM_RETRIEVING_CUSTOMER_INFORMATION);
        Assert.assertEquals("There was a problem retrieving data from database information.", StringsI18N.PROBLEM_RETRIEVING_DATABASE_INFORMATION);
    }

    @SuppressWarnings("unchecked")
    private List<Product> getAvailableProducts() throws IllegalArgumentException
    {
        final Map<String, Object> asMap = this.model.asMap();
        final Collection<Object> values = asMap.values();
        return (List<Product>) values.toArray()[0];
    }
}