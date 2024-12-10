package tdtu.Proptech.controller;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import tdtu.Proptech.dto.PropertyDTO;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.service.listing.ListingService;
import tdtu.Proptech.service.subscription.SubscriptionService;

@Controller
@RequestMapping
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class HomeController {
	ListingService listingService;
	SubscriptionService subscriptionService;

	@GetMapping(value = { "/", "/home", "" })
	public String index(Model model) {
		List<Property> properties = listingService.getSalesProperties();
		List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
		model.addAttribute("properties", propertyDTOs);
		return "index";
	}

	@GetMapping("/properties")
	public String properties(Model model) {

		List<Property> properties = listingService.getSalesProperties();
		List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
		model.addAttribute("properties", propertyDTOs);
		model.addAttribute("title", "All Properties");
		return "properties";
	}

	@GetMapping("/rentalProperties")
	public String rentalProperties(Model model) {

		List<Property> properties = listingService.getRentalProperties();
		List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
		model.addAttribute("properties", propertyDTOs);
		model.addAttribute("title", "Rental Properties");

		return "properties";
	}

	@GetMapping("/sellProperties")
	public String sellProperties(Model model) {

		List<Property> properties = listingService.getSalesProperties();
		List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
		model.addAttribute("properties", propertyDTOs);
		model.addAttribute("title", "Sale Properties");
		return "properties";
	}

	@GetMapping("/properties/single/{id}")
	public String singleProperty(@PathVariable long id, Model model) {
		Property property = listingService.getPropertyById(id);
		PropertyDTO propertyDTO = listingService.converPropertyToPropertyDTO(property);
		model.addAttribute("property", propertyDTO);
		return "property-single";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@GetMapping("/error")
	public String error() {
		return "error";
	}

	@GetMapping("/addProperty")
	public String addProperty() {
		return "add-property";
	}

	@GetMapping("/myProperties")
	public String myProperty(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentEmail = authentication.getName();
		List<Property> properties = listingService.getAllPropertiesByUserEmail(currentEmail);
		List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
		model.addAttribute("properties", propertyDTOs);
		System.out.println(propertyDTOs);
		return "my-properties";
	}

	@GetMapping("/editProperty/{id}")
	public String myProperty(@PathVariable long id, Model model) {
		Property property = listingService.getPropertyById(id);
		PropertyDTO propertyDTO = listingService.converPropertyToPropertyDTO(property);
		model.addAttribute("property", propertyDTO);
		return "editProperty";
	}

	@GetMapping("/sellProcess/{id}")
	public String sellProcess(@PathVariable long id, Model model) {
		Property property = listingService.getPropertyById(id);
		PropertyDTO propertyDTO = listingService.converPropertyToPropertyDTO(property);
		model.addAttribute("property", propertyDTO);
		return "sell-process";
	}

	@GetMapping("/rentalProcess")
	public String rentalProcess() {
		return "rental-process";
	}

	@GetMapping("/search")
	public String search(@RequestParam(required = false) String type, @RequestParam(required = false) String priceRange,
			@RequestParam(required = false) String name, @RequestParam(required = false) String address, Model model) {
		model.addAttribute("title", "Searching for \"" + address + "\"");
		Double minPrice = null;
		Double maxPrice = null;
		if (priceRange != null && !priceRange.isEmpty()) {
			String[] priceParts = priceRange.split("-");
			minPrice = !priceParts[0].isEmpty() ? Double.valueOf(priceParts[0]) : null;
			maxPrice = priceParts.length > 1 && !priceParts[1].isEmpty() ? Double.valueOf(priceParts[1]) : null;
		}
		List<Property> properties = listingService.getPropertiesByCriteria(type, minPrice, maxPrice, name, address);
		List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
		model.addAttribute("properties", propertyDTOs);
		return "search";
	}

	@GetMapping("/payment/{subsriptionId}")
	public String payment(@PathVariable long subsriptionId, Model model) {
		Subscription subscription = subscriptionService.getSubscriptionById(subsriptionId);
		model.addAttribute("subscription", subscription);
		model.addAttribute("price", NumberFormat.getInstance(new Locale("vi", "VN")).format(subscription.getPrice()));
		return "payment";
	}

	@GetMapping("/subscriptions")
	public String subscriptions(Model model) {
		List<Subscription> subscriptions = subscriptionService.getAllSubscription();
		model.addAttribute("subscriptions", subscriptions);
		return "subscriptions";
	}

}
