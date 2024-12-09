package tdtu.Proptech.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import tdtu.Proptech.dto.PropertyDTO;
import tdtu.Proptech.dto.RentalDTO;
import tdtu.Proptech.dto.SalesDTO;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.Rental;
import tdtu.Proptech.model.Sales;
import tdtu.Proptech.service.listing.ListingService;
import tdtu.Proptech.service.rental.RentalService;
import tdtu.Proptech.service.sales.SalesService;

@Controller
@RequestMapping("/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AdminController {
	ListingService listingService;
	RentalService rentalService;
	SalesService salesService;

    @PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = { "/", "" })
	public String index(Model model) {
		List<Property> properties = listingService.getPendingAvailableProperties();
		List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
		model.addAttribute("pendingAvailable", propertyDTOs);
		List<Rental> rentals = rentalService.getPendingRentedProperties();
		List<RentalDTO> rentalsDTO = rentalService.convertRentalsToRentalsDTO(rentals);
		model.addAttribute("pendingRented", rentalsDTO);
		List<Sales> salesList = salesService.getPendingSoldProperties();
		List<SalesDTO> salesListDTO = salesService.convertListSalesToListSalesDTO(salesList);
		model.addAttribute("pedingSold", salesListDTO);
		return "dashboard";
	}

	@GetMapping("/review/{id}")
	public String review(@PathVariable long id, Model model) {
		Property property = listingService.getPropertyById(id);
		PropertyDTO propertyDTO = listingService.converPropertyToPropertyDTO(property);
		model.addAttribute("property", propertyDTO);
		return "review-property";
	}

}
