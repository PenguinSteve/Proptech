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

    PropertyDTO converPropertyToPropertyDTO(Property property);
}