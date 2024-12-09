package tdtu.Proptech.service.rental;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tdtu.Proptech.dto.RentalDTO;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.Rental;
import tdtu.Proptech.repository.PropertyRepository;
import tdtu.Proptech.repository.RentalRepository;
import tdtu.Proptech.request.UploadRentedPropertyRequest;

import tdtu.Proptech.service.listing.ListingService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService{

    private final ListingService listingService;

    private final RentalRepository rentalRepository;

    private final PropertyRepository propertyRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<Rental> getPendingRentedProperties() {
        return rentalRepository.findByPropertyStatus("PENDING_RENTED");
    }

    @Override
    public List<Rental> getRentedProperties() {
        return rentalRepository.findByPropertyStatus("RENTED");
    }

    @Override
    public Rental uploadRentedProperty(String userEmail, UploadRentedPropertyRequest request) {
        Property existingProperty = listingService.getPropertyById(request.getPropertyId());

        if(!userEmail.equals(existingProperty.getRealtor().getEmail())){
            throw new UnauthorizedAccessException("You are not authorized to update rental of this property.");
        }

        if(!"AVAILABLE".equals(existingProperty.getStatus())){
            throw new UnauthorizedAccessException("You are not authorized to update rental of this property.");
        }

        existingProperty.setStatus("PENDING_RENTED");
        Rental rental = createRental(existingProperty, request);
        return rentalRepository.save(rental);
    }

    private Rental createRental(Property existingProperty, UploadRentedPropertyRequest request){
        return new Rental(
                existingProperty,
                existingProperty.getRealtor(),
                request.getRentalDate(),
                request.getRentalDuration(),
                request.getRentalPrice(),
                request.getRenterName(),
                request.getRenterEmail(),
                request.getRenterPhone());
    }


    @Override
    public Rental updatePendingRentedProperty(Long propertyId, String status) {
        Property existingProperty = listingService.getPropertyById(propertyId);
        Rental existingRental = getRentalByPropertyId(propertyId);

        switch (status) {
            case "RENTED":
                if ("PENDING_RENTED".equals(existingProperty.getStatus())) {
                    listingService.updatePendingProperty(propertyId, "RENTED");
                }
                break;
            case "UNAVAILABLE":
                if ("PENDING_RENTED".equals(existingProperty.getStatus())) {
                    listingService.updatePendingProperty(propertyId, "UNAVAILABLE");
                    rentalRepository.deleteById(existingRental.getId());
                    return null;
                }
                break;
            default:
                throw new RuntimeException("Invalid property status: " + status);
        }

        return getRentalByPropertyId(propertyId);
    }

    @Override
    public Rental getRentalByPropertyId(Long propertyId) {
        return rentalRepository.findByPropertyId(propertyId).orElseThrow(() -> new RuntimeException("Rental not found!"));
    }

    @Override
    public RentalDTO convertRentalToRentalDTO(Rental rental) {
        return modelMapper.map(rental, RentalDTO.class);
    }

    @Override
    public List<RentalDTO> convertRentalsToRentalsDTO(List<Rental> rentals) {
        return rentals.stream().map(this :: convertRentalToRentalDTO).toList();
    }
}
