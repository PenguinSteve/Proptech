package tdtu.Proptech.controller.RestAPIController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tdtu.Proptech.dto.PropertyDTO;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.request.UploadPropertyRequest;
import tdtu.Proptech.response.ApiResponse;
import tdtu.Proptech.service.listing.ListingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/listing")
public class ApiListingController {

	private final ListingService listingService;

	@PostMapping("/realtor")
	@PreAuthorize("hasRole('REALTOR')")
	public ResponseEntity<ApiResponse> uploadProperty(UploadPropertyRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentEmail = authentication.getName();
		try {
			return ResponseEntity.ok(new ApiResponse("Property uploaded successfully",
					listingService.converPropertyToPropertyDTO(listingService.uploadProperty(currentEmail, request))));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PutMapping("/realtor/{id}")
	@PreAuthorize("hasRole('REALTOR')")
	public ResponseEntity<ApiResponse> modifyProperty(@PathVariable Long id, UploadPropertyRequest request) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentEmail = authentication.getName();

			return ResponseEntity.ok(new ApiResponse("Property modified successfully", listingService
					.converPropertyToPropertyDTO(listingService.modifyProperty(currentEmail, id, request))));
		} catch (UnauthorizedAccessException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage(), null));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping
	public ResponseEntity<ApiResponse> getProperties() {
		try {
			List<Property> properties = listingService.getAllProperties();
			List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
			return ResponseEntity.ok(new ApiResponse("Properties retrieved successfully", propertyDTOs));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/rental")
	public ResponseEntity<ApiResponse> getRentalProperties() {
		try {
			List<Property> properties = listingService.getRentalProperties();
			List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
			return ResponseEntity.ok(new ApiResponse("Rental properties retrieved successfully", propertyDTOs));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/sales")
	public ResponseEntity<ApiResponse> getSalesProperties() {
		try {
			List<Property> properties = listingService.getSalesProperties();
			List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
			return ResponseEntity.ok(new ApiResponse("Sales properties retrieved successfully", propertyDTOs));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getProperty(@PathVariable Long id) {
		try {
			Property property = listingService.getPropertyById(id);
			PropertyDTO propertyDTO = listingService.converPropertyToPropertyDTO(property);
			return ResponseEntity.ok(new ApiResponse("Property retrieved successfully", propertyDTO));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/realtor/history")
	@PreAuthorize("hasRole('REALTOR')")
	public ResponseEntity<ApiResponse> getRealtorProperties() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentEmail = authentication.getName();

			List<Property> properties = listingService.getAllPropertiesByUserEmail(currentEmail);
			List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
			return ResponseEntity.ok(new ApiResponse("Properties retrieved successfully", propertyDTOs));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/admin/pending")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> getPendingAvailableProperties() {
		try {
			List<Property> properties = listingService.getPendingAvailableProperties();
			List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
			return ResponseEntity.ok(new ApiResponse("Pending properties retrieved successfully", propertyDTOs));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PutMapping("/realtor/{id}/status")
	@PreAuthorize("hasRole('REALTOR')")
	public ResponseEntity<ApiResponse> updateUnavailableProperty(@PathVariable Long id, @RequestBody String status) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentEmail = authentication.getName();

			if (!"UNAVAILABLE".equals(status)) {
				throw new RuntimeException("This API does not support status other than UNAVAILABLE.");
			}
			PropertyDTO property = listingService
					.converPropertyToPropertyDTO(listingService.updateStatusProperty(currentEmail, id, status));

			return ResponseEntity.ok(new ApiResponse("Property's status updated successfully", property));
		} catch (UnauthorizedAccessException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage(), null));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	/***
	 * This API only receive UNAVAILABLE for disapproval of a posting property and
	 * AVAILABLE for approval of a posting property
	 ***/
	@PutMapping("/admin/pending/{id}/status")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> updatePendingAvailableProperty(@PathVariable Long id,
			@RequestParam String status) {
		try {
			System.out.println(status);
			if (!"UNAVAILABLE".equals(status) && !"AVAILABLE".equals(status)) {
				throw new RuntimeException("This API does not support status other than UNAVAILABLE and AVAILABLE.");
			}
			PropertyDTO property = listingService
					.converPropertyToPropertyDTO(listingService.updatePendingProperty(id, status));

			return ResponseEntity.ok(new ApiResponse("Property's status updated successfully", property));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse> searchProperties(@RequestParam(required = false) String type,
			@RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) String name, @RequestParam(required = false) String address) {
		try {
			List<Property> properties = listingService.getPropertiesByCriteria(type, minPrice, maxPrice, name, address);
			List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
			return ResponseEntity.ok(new ApiResponse("Properties retrieved successfully", propertyDTOs));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}
}