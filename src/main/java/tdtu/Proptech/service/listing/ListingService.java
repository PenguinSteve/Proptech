package tdtu.Proptech.service.listing;

import tdtu.Proptech.dto.PropertyDTO;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.request.UploadPropertyRequest;

import java.util.List;

public interface ListingService {
    List<Property> getAllProperties();
    List<Property> getSalesProperties();
    List<Property> getRentalProperties();
    Property getPropertyById(Long id);
    Property uploadProperty(String userEmail, UploadPropertyRequest request);
    Property modifyProperty(String userEmail, Long id, UploadPropertyRequest request);
    List<Property> getPropertiesByCriteria(String type, Double minPrice, Double maxPrice, String name, String address);
    List<Property> getAllPropertiesByUserEmail(String realtorEmail);
    Property updatePendingProperty(Long id, String type);
    Property updateStatusProperty(String userEmail, Long id, String type);
    List<Property> getPendingAvailableProperties();


    PropertyDTO converPropertyToPropertyDTO(Property property);
    List<PropertyDTO> convertPropetiesToPropertiesDTO(List<Property> properties);
}