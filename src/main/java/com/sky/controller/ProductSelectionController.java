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

	/**
	 * Method GET for retrieving available products by the @param customerId.
	 * Exceptions could be throw and the Exception Handling make the choice
	 * to the next view page and the message to informing the user.
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
	final String customerIdParam, final Model model) throws CustomerNotFoundException, IllegalArgumentException, InvalidLocationException
	{
		this.customerId = Long.valueOf(customerIdParam);

		final int locationId = this.customerLocationService.getCustomerLocationId(this.customerId);

		final List<Product> availableProducts = this.catalogueService.getAvailableProducts(locationId);

		model.addAttribute("availableProducts", availableProducts);

		return "productSelection";
	}

	/**
	 * Method GET for retrieving products by the choice of user to confirm the page.
	 * JPA was configured by default by Spring Boot as a memory-based database, so
	 * I decided not to build a cache of products. As we can see, always in the loop
	 * is used the "findOne" method to find a product.
	 *
	 * @param baskeHidden extension of the Map interface that stores multiple values that was put by the view.
	 * @param model used by Spring to handling controller and view. Is put customerId and products to showing
	 * in the view.
	 * @return template html name
	 */
	@RequestMapping(value = "confirmationPage/", method = RequestMethod.GET)
	public String confirmationPage(@RequestParam
			final MultiValueMap<String, Object> baskeHidden, final Model model)
	{
		final List<Product> products = new ArrayList<>();
		final Collection<List<Object>> baskestList = baskeHidden.values();

		for (final List<Object> subListBasket : baskestList)
		{
			if (subListBasket.isEmpty())
				continue;

			final String productId = (String) subListBasket.get(0);
			if (productId.isEmpty())
				continue;

			final Product product = this.productRepository.findOne(Long.valueOf(productId));
			products.add(product);
		}

		model.addAttribute("customerId", this.customerId);
		model.addAttribute("products", products);
		return "confirmationPage";
	}

	@RequestMapping(value = "finalizeSelection", method = RequestMethod.POST)
	public String finalizeSelection()
	{
		// Would save the confirmation... This explain the method POST
		return "index";
	}
}