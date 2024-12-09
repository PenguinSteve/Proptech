package tdtu.Proptech.service.sales;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tdtu.Proptech.dto.PropertyDTO;
import tdtu.Proptech.dto.SalesDTO;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.Sales;
import tdtu.Proptech.repository.PropertyRepository;
import tdtu.Proptech.repository.SalesRepository;
import tdtu.Proptech.request.UploadSoldPropertyRequest;
import tdtu.Proptech.service.listing.ListingService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService{
    private final ListingService listingService;

    private final SalesRepository salesRepository;

    private final PropertyRepository propertyRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<Sales> getPendingSoldProperties() {
        return salesRepository.findByPropertyStatus("PENDING_SOLD");
    }

    @Override
    public List<Sales> getSoldProperties() {
        return salesRepository.findByPropertyStatus("SOLD");
    }

    @Override
    public Sales uploadSoldProperty(String userEmail, UploadSoldPropertyRequest request) {
        Property existingProperty = listingService.getPropertyById(request.getPropertyId());

        if(!userEmail.equals(existingProperty.getRealtor().getEmail())){
            throw new UnauthorizedAccessException("You are not authorized to update sales of this property.");
        }

        if(!"AVAILABLE".equals(existingProperty.getStatus())){
            throw new UnauthorizedAccessException("You are not authorized to update sales of this property.");
        }

        existingProperty.setStatus("PENDING_SOLD");
        Sales sales = createSales(existingProperty, request);
        System.out.println(sales);
        return salesRepository.save(sales);
    }

    private Sales createSales(Property existingProperty, UploadSoldPropertyRequest request){
        return new Sales(
                existingProperty,
                existingProperty.getRealtor(),
                request.getSalesDate(),
                request.getSalesPrice(),
                request.getBuyerName(),
                request.getBuyerEmail(),
                request.getBuyerPhone());
    }


    @Override
    public Sales updatePendingSoldProperty(Long propertyId, String status) {
        Property existingProperty = listingService.getPropertyById(propertyId);
        Sales existingSales = getSalesByPropertyId(propertyId);

        switch (status) {
            case "SOLD":
                if ("PENDING_SOLD".equals(existingProperty.getStatus())) {
                    listingService.updatePendingProperty(propertyId, "SOLD");
                }
                break;
            case "UNAVAILABLE":
                if ("PENDING_SOLD".equals(existingProperty.getStatus())) {
                    listingService.updatePendingProperty(propertyId, "UNAVAILABLE");
                    salesRepository.deleteById(existingSales.getId());
                    return null;
                }
                break;
            default:
                throw new RuntimeException("Invalid property status: " + status);
        }

        return getSalesByPropertyId(propertyId);
    }

    @Override
    public Sales getSalesByPropertyId(Long propertyId) {
        return salesRepository.findByPropertyId(propertyId).orElseThrow(() -> new RuntimeException("Sales not found!"));
    }

    @Override
    public SalesDTO convertSalesToSalesDTO(Sales sales) {
        return modelMapper.map(sales, SalesDTO.class);
    }

    @Override
    public List<SalesDTO> convertListSalesToListSalesDTO(List<Sales> sales) {
        return sales.stream().map(this :: convertSalesToSalesDTO).toList();
    }
}
