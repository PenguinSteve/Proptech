package tdtu.Proptech.controller.RestAPIController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tdtu.Proptech.dto.PropertyDTO;
import tdtu.Proptech.dto.SalesDTO;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.Sales;
import tdtu.Proptech.request.UploadSoldPropertyRequest;
import tdtu.Proptech.response.ApiResponse;
import tdtu.Proptech.service.listing.ListingService;
import tdtu.Proptech.service.sales.SalesService;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class ApiSalesController {

    private final SalesService salesService;

    private final ListingService listingService;

    @GetMapping("/admin/pending/sales")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getPendingSoldProperties() {
        try {
            List<Property> properties = salesService.getPendingSoldProperties();
            List<PropertyDTO> propertyDTO = listingService.convertPropetiesToPropertiesDTO(properties);
            return ResponseEntity.ok(new ApiResponse("Pending sold properties retrieved successfully", propertyDTO));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getSoldProperties() {
        try {
            List<Sales> sales = salesService.getSoldProperties();
            List<SalesDTO> salesDTO = salesService.convertListSalesToListSalesDTO(sales);
            return ResponseEntity.ok(new ApiResponse("Sold properties retrieved successfully", salesDTO));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/realtor/upload")
    @PreAuthorize("hasRole('REALTOR')")
    public ResponseEntity<ApiResponse> uploadSoldProperty(@RequestBody UploadSoldPropertyRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = authentication.getName();

            return ResponseEntity.ok(new ApiResponse("Upload sold property successfully", salesService.convertSalesToSalesDTO(salesService.uploadSoldProperty(currentEmail, request))));
        }
        catch (UnauthorizedAccessException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage(), null));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/admin/pending/sales/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updatePendingSoldProperty(@PathVariable Long id, @RequestBody String status){
        try{
            if(!"UNAVAILABLE".equals(status) && !"SOLD".equals(status)){
                throw new RuntimeException("This API does not support status other than UNAVAILABLE and SOLD.");
            }
            Sales sales = salesService.updatePendingSoldProperty(id, status);

            if(sales == null){
                return ResponseEntity.ok(new ApiResponse("Property was canceled successfully", null));
            }
            return ResponseEntity.ok(new ApiResponse("Property was approved successfully", salesService.convertSalesToSalesDTO(sales)));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getSales(@PathVariable Long id) {
        try {
            Sales sales = salesService.getSalesByPropertyId(id);
            SalesDTO salesDTO = salesService.convertSalesToSalesDTO(sales);
            return ResponseEntity.ok(new ApiResponse("Sales retrieved successfully", salesDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
