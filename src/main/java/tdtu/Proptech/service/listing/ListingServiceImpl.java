package tdtu.Proptech.service.listing;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tdtu.Proptech.dto.PropertyDTO;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.model.Image;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.User;
import tdtu.Proptech.repository.PropertyRepository;
import tdtu.Proptech.request.UploadPropertyRequest;
import tdtu.Proptech.service.image.ImageService;
import tdtu.Proptech.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService{

    private final PropertyRepository propertyRepository;

    private final UserService userService;

    private final ImageService imageService;

    private final ModelMapper modelMapper;

    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @Override
    public List<Property> getSalesProperties() {
        return propertyRepository.findByType("SALES");
    }

    @Override
    public List<Property> getRentalProperties() {
        return propertyRepository.findByType("RENTAL");
    }

    @Override
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id).orElseThrow(() -> new RuntimeException("Property not found!"));
    }

    @Override
    public Property uploadProperty(String userEmail, UploadPropertyRequest request) {
        User existingUser = userService.getUserByEmail(userEmail);

        Property newProperty = createProperty(existingUser, request);

        Property createdProperty = propertyRepository.save(newProperty);

        List<MultipartFile> images = request.getImages();

        List<Image> savedImages = imageService.saveImages(createdProperty, images);

        createdProperty.setImages(savedImages);

        return propertyRepository.save(createdProperty);
    }
    private Property createProperty(User existingUser, UploadPropertyRequest request){
        return new Property(
                request.getName(),
                request.getAddress(),
                request.getPrice(),
                request.getType(),
                "AVAILABLE",
                existingUser
        );
    }

    @Override
    public Property modifyProperty(String userEmail, Long id, UploadPropertyRequest request) {
        Property existingProperty = getPropertyById(id);

        if(!userEmail.equals(existingProperty.getRealtor().getEmail())){
            throw new UnauthorizedAccessException("You are not authorized to update this property.");
        }

        Property updatedProperty = updateProperty(existingProperty, request);

        return propertyRepository.save(updatedProperty);
    }

    private Property updateProperty(Property existingProperty, UploadPropertyRequest request){
        existingProperty.setAddress(request.getAddress());
        existingProperty.setName(request.getName());
        existingProperty.setPrice(request.getPrice());
        existingProperty.setType(request.getType());
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            // Delete existing images from the database
            for (Image image : existingProperty.getImages()) {
                imageService.deleteImage(image);
            }
            // Clear the list of images
            existingProperty.getImages().clear();
            // Save new images
            List<Image> savedImages = imageService.saveImages(existingProperty, request.getImages());
            existingProperty.getImages().addAll(savedImages);
        }
        return existingProperty;
    }

    @Override
    public List<Property> getPropertiesByCriteria(String type, Double minPrice, Double maxPrice, String name, String address) {
        return propertyRepository.findPropertiesByCriteria(type, minPrice, maxPrice, name, address);
    }

    @Override
    public PropertyDTO converPropertyToPropertyDTO(Property property) {
        return modelMapper.map(property, PropertyDTO.class);
    }
}