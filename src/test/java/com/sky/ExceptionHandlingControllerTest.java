package com.sky;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sky.controller.ExceptionHandlingController;
import com.sky.exception.CustomerNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ExceptionHandlingControllerTest
{
    @Autowired
    private TestRestTemplate restTemplate;

    private ExceptionHandlingController exceptionHandlingController;

    @Before
    public void setup()
    {
        this.exceptionHandlingController = new ExceptionHandlingController();
    }

    @Test
    public void testCalls()
    {
        this.exceptionHandlingController.exception(new Exception());
        this.exceptionHandlingController.handleCustomerNotFoundAndInvalidLocationException(new CustomerNotFoundException());
        this.exceptionHandlingController.handleDatabaseException(new SQLException());
    }
}