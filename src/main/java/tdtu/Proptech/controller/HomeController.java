package tdtu.Proptech.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import tdtu.Proptech.dto.PropertyDTO;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.service.listing.ListingService;

@Controller
@RequestMapping
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class HomeController {
	ListingService listingService;

	@GetMapping(value = { "/", "/home", "" })
	public String index() {
		return "index";
	}

	@GetMapping("/properties")
	public String properties(Model model) {

		List<Property> properties = listingService.getSalesProperties();
		List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
		model.addAttribute("properties", propertyDTOs);
		return "properties";
	}

	@GetMapping("/properties/single/{id}")
	public String singleProperty(@PathVariable long id, Model model) {
		Property property = listingService.getPropertyById(id);
        PropertyDTO propertyDTO = listingService.converPropertyToPropertyDTO(property);
        model.addAttribute("property", propertyDTO);
		return "property-single";
	}

	@GetMapping("cart/checkout")
	public String checkout() {
		return "checkout";
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
	public String myProperty() {
		return "my-properties";
	}

	@GetMapping("/sellProcess")
	public String sellProcess() {
		return "sell-process";
	}

	@GetMapping("/rentalProcess")
	public String rentalProcess() {
		return "rental-process";
	}

	@GetMapping("/subscriptions")
	public String subscriptions() {
		return "subscriptions";
	}

}
