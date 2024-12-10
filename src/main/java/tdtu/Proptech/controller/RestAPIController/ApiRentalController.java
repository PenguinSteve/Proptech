package tdtu.Proptech.controller.RestAPIController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tdtu.Proptech.dto.RentalDTO;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.model.Rental;
import tdtu.Proptech.request.UploadRentedPropertyRequest;
import tdtu.Proptech.response.ApiResponse;
import tdtu.Proptech.service.rental.RentalService;

import java.util.List;

@RestController
@RequestMapping("/api/rental")
@RequiredArgsConstructor
public class ApiRentalController {

    private final RentalService rentalService;

    @GetMapping("/admin/pending/rental")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getPendingRentedProperties() {
        try {
            List<Rental> rentals = rentalService.getPendingRentedProperties();
            List<RentalDTO> rentalsDTO = rentalService.convertRentalsToRentalsDTO(rentals);
            return ResponseEntity.ok(new ApiResponse("Pending rented properties retrieved successfully", rentalsDTO));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getRentedProperties() {
        try {
            List<Rental> rental = rentalService.getRentedProperties();
            List<RentalDTO> rentalsDTO = rentalService.convertRentalsToRentalsDTO(rental);
            return ResponseEntity.ok(new ApiResponse("Rented properties retrieved successfully", rentalsDTO));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/realtor/upload")
    @PreAuthorize("hasRole('REALTOR')")
    public ResponseEntity<ApiResponse> uploadRentedProperty(@RequestBody UploadRentedPropertyRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = authentication.getName();

            return ResponseEntity.ok(new ApiResponse("Upload rented property successfully", rentalService.convertRentalToRentalDTO(rentalService.uploadRentedProperty(currentEmail, request))));
        }
        catch (UnauthorizedAccessException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage(), null));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/admin/pending/rental/{propertyId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updatePendingRentedProperty(@PathVariable Long propertyId, @RequestParam String status){
        try{
            if(!"UNAVAILABLE".equals(status) && !"RENTED".equals(status)){
                throw new RuntimeException("This API does not support status other than UNAVAILABLE and RENTED.");
            }
            Rental rental = rentalService.updatePendingRentedProperty(propertyId, status);

            if(rental == null){
                return ResponseEntity.ok(new ApiResponse("Property was canceled successfully", true));
            }
            return ResponseEntity.ok(new ApiResponse("Property was approved successfully", rentalService.convertRentalToRentalDTO(rental)));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/admin/{propertyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getRental(@PathVariable Long propertyId) {
        try {
            Rental rental = rentalService.getRentalByPropertyId(propertyId);
            RentalDTO salesDTO = rentalService.convertRentalToRentalDTO(rental);
            return ResponseEntity.ok(new ApiResponse("Rental retrieved successfully", salesDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
