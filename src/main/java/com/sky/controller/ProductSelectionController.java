package com.sky.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(value = "customerLocation/", method = RequestMethod.GET)
    public String getCustomerLocation(@RequestParam("customerId")
    final String customerIdParam, final Model model)
    {
        try
        {
            this.customerId = Long.valueOf(customerIdParam);

            final int locationId = this.customerLocationService.getCustomerLocationId(this.customerId);

            final List<Product> availableProducts = this.catalogueService.getAvailableProducts(locationId);

            model.addAttribute("availableProducts", availableProducts);
        }
        catch (final CustomerNotFoundException | InvalidLocationException e)
        {
            model.addAttribute("errorMessage", StringsI18N.PROBLEM_RETRIEVING_CUSTOMER_INFORMATION);
            return "index";
        }
        catch (final NumberFormatException e)
        {
            model.addAttribute("errorMessage", StringsI18N.PROBLEM_READING_CUSTOMER_ID);
            return "index";
        }

        return "productSelection";
    }

    @RequestMapping(value = "confirmationPage/", method = RequestMethod.GET)
    public String confirmationPage(@RequestParam
        final MultiValueMap<String, Object> baskeHidden, final Model model)
    {
        if (this.customerId == null || baskeHidden == null)
            return "index";

        final List<Product> products = new ArrayList<>();
        final Collection<List<Object>> values = baskeHidden.values();

        try
        {
            for (final List<Object> list : values)
            {
                if (list.isEmpty())
                    continue;

                final String value = (String) list.get(0);
                if (value.isEmpty())
                    continue;

                final Long id = Long.valueOf(value);
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

    @RequestMapping(value = "finalizeSelection", method = RequestMethod.POST)
    public String finalizeSelection()
    {
        // Would save the confirmation... That's why method POST
        return "index";
    }
}