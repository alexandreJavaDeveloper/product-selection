package com.sky;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sky.exception.CustomerNotFoundException;
import com.sky.repository.CustomerRepository;
import com.sky.service.CustomerLocationService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class CustomerLocationServiceTest
{
    @Autowired
    private CustomerRepository customerRepository;

    private CustomerLocationService customerLocationService;

    @Before
    public void setup()
    {
        this.customerLocationService = new CustomerLocationService(this.customerRepository);
    }

    @Test
    public void testValidParam() throws CustomerNotFoundException
    {
        final Long customerId = new Long(1);
        final int locationId = this.customerLocationService.getCustomerLocationId(customerId);
        Assert.assertEquals(2, locationId);
    }

    @Test
    public void testValidParam2() throws CustomerNotFoundException
    {
        final Long customerId = new Long(4);
        final int locationId = this.customerLocationService.getCustomerLocationId(customerId);
        Assert.assertEquals(1, locationId);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testInvalidParam() throws CustomerNotFoundException
    {
        final Long customerId = new Long(-1);
        this.customerLocationService.getCustomerLocationId(customerId);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testInvalidParam2() throws CustomerNotFoundException
    {
        final Long customerId = new Long(Long.MAX_VALUE);
        this.customerLocationService.getCustomerLocationId(customerId);
    }
}