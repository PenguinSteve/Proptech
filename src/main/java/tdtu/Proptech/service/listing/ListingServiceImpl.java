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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService{

    private final PropertyRepository propertyRepository;

    private final UserService userService;

    private final ImageService imageService;

    private final ModelMapper modelMapper;

    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAll().stream()
                .filter(property -> property.getExpire() == null || property.getExpire().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Property> getSalesProperties() {
        return propertyRepository.findByType("SALES").stream()
                .filter(property -> property.getExpire() == null || property.getExpire().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Property> getRentalProperties() {
        return propertyRepository.findByType("RENTAL").stream()
                .filter(property -> property.getExpire() == null || property.getExpire().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
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
    private Property createProperty(User existingUser, UploadPropertyRequest request) {
        if (!existingUser.isSubscriptionActive()) {
            throw new RuntimeException("User subscription is not active.");
        }

        long unexpiredPropertiesCount = existingUser.getProperties().stream()
                .filter(property -> property.getExpire() == null || property.getExpire().isAfter(LocalDateTime.now()))
                .count();

        String planName = existingUser.getSubscription().getPlanName();
        int maxProperties;
        int expireDays;

        switch (planName) {
            case "STARTER":
                maxProperties = 5;
                expireDays = 5;
                break;
            case "BASIC":
                maxProperties = 10;
                expireDays = 14;
                break;
            case "STANDARD":
                maxProperties = 20;
                expireDays = 21;
                break;
            case "VIP":
                maxProperties = Integer.MAX_VALUE; // No limit
                expireDays = 30;
                break;
            default:
                throw new RuntimeException("Unknown subscription plan.");
        }

        if (unexpiredPropertiesCount >= maxProperties) {
            throw new RuntimeException("User has reached the maximum number of unexpired properties for their subscription plan.");
        }

        LocalDateTime expireDate = LocalDateTime.now().plusDays(expireDays);

        return new Property(
                request.getName(),
                request.getAddress(),
                request.getPrice(),
                request.getArea(),
                request.getType(),
                "AVAILABLE",
                existingUser,
                expireDate
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

    private Property updateProperty(Property existingProperty, UploadPropertyRequest request) {
        User existingUser = existingProperty.getRealtor();

        if (!existingUser.isSubscriptionActive()) {
            throw new RuntimeException("User subscription is not active.");
        }

        long unexpiredPropertiesCount = existingUser.getProperties().stream()
                .filter(property -> property.getExpire() == null || property.getExpire().isAfter(LocalDateTime.now()))
                .count();

        String planName = existingUser.getSubscription().getPlanName();
        int maxProperties;
        int expireDays;

        switch (planName) {
            case "STARTER":
                maxProperties = 5;
                expireDays = 5;
                break;
            case "BASIC":
                maxProperties = 10;
                expireDays = 14;
                break;
            case "STANDARD":
                maxProperties = 20;
                expireDays = 21;
                break;
            case "VIP":
                maxProperties = Integer.MAX_VALUE; // No limit
                expireDays = 30;
                break;
            default:
                throw new RuntimeException("Unknown subscription plan.");
        }

        if (unexpiredPropertiesCount > maxProperties) {
            throw new RuntimeException("User has reached the maximum number of unexpired properties for their subscription plan.");
        }

        existingProperty.setAddress(request.getAddress());
        existingProperty.setName(request.getName());
        existingProperty.setPrice(request.getPrice());
        existingProperty.setArea(request.getArea());
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

        existingProperty.setExpire(LocalDateTime.now().plusDays(expireDays));

        return existingProperty;
    }

    @Override
    public List<Property> getPropertiesByCriteria(String type, Double minPrice, Double maxPrice, String name, String address) {
        return propertyRepository.findPropertiesByCriteria(type, minPrice, maxPrice, name, address).stream()
                .filter(property -> property.getExpire() == null || property.getExpire().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Property> getAllPropertiesByUserEmail(String realtorEmail) {
        return propertyRepository.findByRealtorEmail(realtorEmail);
    }

    @Override
    public List<PropertyDTO> convertPropetiesToPropertiesDTO(List<Property> properties){
        return properties.stream().map(this :: converPropertyToPropertyDTO).toList();
    }

    @Override
    public PropertyDTO converPropertyToPropertyDTO(Property property) {
        return modelMapper.map(property, PropertyDTO.class);
    }
}