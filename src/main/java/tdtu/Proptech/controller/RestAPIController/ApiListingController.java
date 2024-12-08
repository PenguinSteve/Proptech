package tdtu.Proptech.controller.RestAPIController;

import com.cloudinary.Api;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tdtu.Proptech.dto.PropertyDTO;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.request.UploadPropertyRequest;
import tdtu.Proptech.response.ApiResponse;
import tdtu.Proptech.service.listing.ListingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/listing")
public class ApiListingController {

    private final ListingService listingService;
    
    @PostMapping
    public ResponseEntity<ApiResponse> uploadProperty(UploadPropertyRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        try{
            return ResponseEntity.ok(new ApiResponse("Property uploaded successfully", listingService.converPropertyToPropertyDTO(listingService.uploadProperty(currentEmail, request))));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> modifyProperty(@PathVariable Long id, UploadPropertyRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        try {
            return ResponseEntity.ok(new ApiResponse("Property modified successfully", listingService.converPropertyToPropertyDTO(listingService.modifyProperty(currentEmail, id, request))));
        }
        catch (UnauthorizedAccessException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage(), null));
        }
        catch (RuntimeException e){
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

    @GetMapping("/user/history")
    public ResponseEntity<ApiResponse> getRealtorProperties() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        try {
            List<Property> properties = listingService.getAllPropertiesByUserEmail(currentEmail);
            List<PropertyDTO> propertyDTOs = listingService.convertPropetiesToPropertiesDTO(properties);
            return ResponseEntity.ok(new ApiResponse("Properties retrieved successfully", propertyDTOs));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
