package tdtu.Proptech.controller.RestAPIController;

import com.cloudinary.Api;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.request.UploadPropertyRequest;
import tdtu.Proptech.response.ApiResponse;
import tdtu.Proptech.service.listing.ListingService;

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
}
