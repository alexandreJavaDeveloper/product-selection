package com.sky.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sky.entity.Product;
import com.sky.exception.CustomerNotFoundException;
import com.sky.exception.InvalidLocationException;
import com.sky.i18n.StringsI18N;
import com.sky.mock.RepositoryMock;
import com.sky.repository.CustomerRepository;
import com.sky.repository.ProductRepository;
import com.sky.service.CatalogueService;
import com.sky.service.CustomerLocationService;

@Controller
@RequestMapping("/productselection")
public class ProductSelectionController
{
    private final CustomerLocationService customerLocationService;

    private final CatalogueService catalogueService;

    private final ProductRepository productRepository;

    private Long customerId;

    @Autowired
    public ProductSelectionController(final CustomerRepository customerRepository, final ProductRepository productRepository)
    {
        this.productRepository = productRepository;
        this.customerLocationService = new CustomerLocationService(customerRepository);
        this.catalogueService = new CatalogueService(productRepository);

        RepositoryMock.initializeFactory(customerRepository, productRepository);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index()
    {
        return "index";
    }

    @RequestMapping(value = "customerLocation", method = RequestMethod.POST)
    public String getCustomerLocation(@RequestBody
        final MultiValueMap<String, Object> customer, final Model model)
    {
        try
        {
            this.customerId = this.extractCustomerId(customer);

            final int locationId = this.customerLocationService.getCustomerLocationId(this.customerId);

            final List<Product> availableProducts = this.catalogueService.getAvailableProducts(locationId);

            model.addAttribute("availableProducts", availableProducts);
        }
        catch (final CustomerNotFoundException | InvalidLocationException e)
        {
            model.addAttribute("errorMessage", StringsI18N.PROBLEM_RETRIEVING_CUSTOMER_INFORMATION);
            return "index";
        }

        return "productSelection";
    }

    @RequestMapping(value = "confirmationPage", method = RequestMethod.POST)
    public String confirmationPage(@RequestBody
        final MultiValueMap<String, Object> confirmationPage, final Model model)
    {
        if (this.customerId == null || confirmationPage == null)
            return "index";

        final List<Product> products = new ArrayList<>();

        try
        {
            final Collection<List<Object>> values = confirmationPage.values();

            for (final List<Object> list : values)
            {
                final Long id = Long.valueOf((String) list.get(0));
                final Product product = this.productRepository.findOne(id);
                products.add(product);
            }
        }
        catch (final Exception e)
        {
            model.addAttribute("errorMessage", StringsI18N.PROBLEM_RETRIEVING_PRODUCTS_TO_CONFIRMATION);
            return "productSelection";
        }

        model.addAttribute("customerId", this.customerId);
        model.addAttribute("products", products);

        return "confirmationPage";
    }

    private Long extractCustomerId(final MultiValueMap<String, Object> customer) throws CustomerNotFoundException
    {
        final String extractedIdValue = (String) customer.get("customerId").get(0);
        try
        {
            return Long.valueOf(extractedIdValue);
        }
        catch (final NumberFormatException e)
        {
            throw new CustomerNotFoundException();
        }
    }
}