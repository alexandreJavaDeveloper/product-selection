package com.sky.service;

import com.sky.entity.Customer;
import com.sky.exception.CustomerNotFoundException;
import com.sky.repository.CustomerRepository;

public class CustomerLocationService
{
    private final CustomerRepository customerRepository;

    public CustomerLocationService(final CustomerRepository customerRepository)
    {
        this.customerRepository = customerRepository;
    }

    public int getCustomerLocationId(final Long customerId) throws CustomerNotFoundException
    {
        final Customer customer = this.customerRepository.findCustomerLocationById(customerId);

        if (customer == null)
            throw new CustomerNotFoundException();

        return customer.getLocationId();
    }
}