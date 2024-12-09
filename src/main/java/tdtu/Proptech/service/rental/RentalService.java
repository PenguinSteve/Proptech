package tdtu.Proptech.service.rental;

import tdtu.Proptech.dto.RentalDTO;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.Rental;
import tdtu.Proptech.request.UploadRentedPropertyRequest;

import java.util.List;

public interface RentalService {
    List<Rental> getPendingRentedProperties();
    List<Rental> getRentedProperties();
    Rental uploadRentedProperty(String userEmail, UploadRentedPropertyRequest request);
    Rental updatePendingRentedProperty(Long propertyId, String status);
    Rental getRentalByPropertyId(Long propertyId);

    RentalDTO convertRentalToRentalDTO(Rental rental);
    List<RentalDTO> convertRentalsToRentalsDTO(List<Rental> rentals);
}
