package com.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sky.entity.Customer;
import com.sky.exception.CustomerNotFoundException;

public interface CustomerRepository extends JpaRepository<Customer, Long>
{
    Customer findCustomerLocationById(Long customerId) throws CustomerNotFoundException;
}