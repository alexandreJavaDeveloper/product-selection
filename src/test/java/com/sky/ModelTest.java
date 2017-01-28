package com.sky;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sky.entity.Customer;
import com.sky.entity.Product;
import com.sky.model.Category;
import com.sky.model.Location;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ModelTest
{
    @Test
    public void testCustomerBehavior()
    {
        final Customer c = new Customer();
        c.hashCode();
        final Customer customer = new Customer();
        customer.setId(new Long(1));
        Assert.assertEquals("1", customer.toString());
        customer.hashCode();

        final Customer customer2 = new Customer(Location.LONDON);
        Assert.assertEquals(Location.LONDON.getName(), customer2.toString());
        customer2.setId(new Long(3));
        customer2.hashCode();

        Assert.assertFalse(customer.equals(customer2));

        customer2.setId(new Long(1));
        Assert.assertTrue(customer.equals(customer2));
        Assert.assertFalse(customer.equals(null));
    }

    @Test
    public void testProductBehavior()
    {
        final Product p = new Product();
        p.hashCode();
        final Product product = new Product("Test Sky", Category.SPORTS);
        product.setId(new Long(1));
        Assert.assertEquals("Test Sky", product.toString());
        Assert.assertEquals("Test Sky", product.getName());
        Assert.assertEquals(Category.SPORTS, product.getCategory());
        Assert.assertTrue(product.isSportCategory());
        Assert.assertFalse(product.isNewsCategory());
        product.hashCode();

        final Product product2 = new Product("Test Sky2", Category.NEWS);
        product2.setId(new Long(2));
        Assert.assertEquals("Test Sky2", product2.toString());
        Assert.assertEquals(Category.NEWS, product2.getCategory());
        product2.hashCode();

        Assert.assertFalse(product.equals(product2));

        product2.setId(new Long(1));
        Assert.assertTrue(product2.equals(product2));
        Assert.assertFalse(product2.equals(null));

        Assert.assertEquals(1, product.compareTo(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocationBehavior()
    {
        Location.getLocationName(-1);
    }

    @Test
    public void testLocationBehavior()
    {
        Assert.assertEquals(2, Location.values().length);
        Assert.assertEquals(Location.LONDON, Location.valueOf(Location.LONDON.getName().toUpperCase()));
    }

    @Test
    public void testCategoryBehavior()
    {
        Assert.assertEquals(2, Category.NEWS.getId());
        Assert.assertEquals("Sports", Category.SPORTS.getName());

        Assert.assertEquals(2, Category.values().length);
        Assert.assertEquals(Category.NEWS, Category.valueOf(Category.NEWS.getName().toUpperCase()));
    }
}