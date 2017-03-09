package com.sky.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sky.entity.Product;
import com.sky.exception.CustomerNotFoundException;
import com.sky.exception.InvalidLocationException;
import com.sky.exception.ProductNotfoundException;
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

    private final Map<Long, Set<Product>> mapAvailableProducts;

    @Autowired
    public ProductSelectionController(final CustomerRepository customerRepository, final ProductRepository productRepository)
    {
        this.mapAvailableProducts = new HashMap<>();

        this.customerLocationService = new CustomerLocationService(customerRepository);

        this.catalogueService = new CatalogueService(productRepository);

        RepositoryMock.initializeFactory(customerRepository, productRepository);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index()
    {
        return "index";
    }

    /**
     * Method GET for retrieving available products by the @param customerId.
     * Exceptions could be throw and the Exception Handling make the choice
     * to the next view page and the message to informing the user.
     *
     * The availableProducts list is put in the map for retrieving if necessary in the next stages.
     *
     * @param customerIdParam value informed by user
     * @param model used by Spring to handling controller and view. Here is put the list of available products
     * to showing in the view
     * @return template html name
     * @throws CustomerNotFoundException
     * @throws IllegalArgumentException
     * @throws InvalidLocationException
     */
    @RequestMapping(value = "availableProducts/", method = RequestMethod.GET)
    public String getAvailableProducts(@RequestParam("customerId")
    final Long customerId, final Model model) throws CustomerNotFoundException, IllegalArgumentException, InvalidLocationException
    {
        final int locationId = this.customerLocationService.getCustomerLocationId(customerId);

        Set<Product> availableProducts = this.mapAvailableProducts.get(customerId);

        if (availableProducts == null)
        {
            availableProducts = new HashSet<>(this.catalogueService.getAvailableProducts(locationId));

            this.mapAvailableProducts.put(customerId, availableProducts);
        }

        model.addAttribute("availableProducts", availableProducts);

        model.addAttribute("customerId", customerId);

        return "productSelection";
    }

    /**
     * Method GET for retrieving products by the choice of user to confirm the page.
     * Use of Map (cache) without need to use database again for retrieving data of Products.
     *
     * @param baskeHidden extension of the Map interface that stores multiple values that was put by the view.
     * @param model used by Spring to handling controller and view. Is put customerId and products to showing
     * in the view.
     * @return template html name
     * @throws ProductNotfoundException
     */
    @RequestMapping(value = "confirmationPage/", method = RequestMethod.GET)
    public String confirmationPage(@RequestParam("customerId")
    final Long customerId, @RequestParam("basket")
    final Long[] basket, final Model model) throws ProductNotfoundException
    {
        final Set<Product> availableProducts = this.mapAvailableProducts.get(customerId);

        final List<Product> chosenProducts = new ArrayList<>();

        for (int i = 0; i < basket.length; i++)
        {
            final Long localCustomerId = basket[i];
            final Product product = this.catalogueService.findOne(localCustomerId);

            if (availableProducts.contains(product))
                chosenProducts.add(product);
        }

        model.addAttribute("products", chosenProducts);

        model.addAttribute("customerId", customerId);

        return "confirmationPage";
    }

    @RequestMapping(value = "finalizeSelection", method = RequestMethod.POST)
    public String finalizeSelection()
    {
        // Would save the confirmation... This explain the method POST
        return "index";
    }
}