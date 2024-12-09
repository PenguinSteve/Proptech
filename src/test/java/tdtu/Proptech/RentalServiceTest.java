package tdtu.Proptech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import tdtu.Proptech.dto.RentalDTO;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.Rental;
import tdtu.Proptech.model.User;
import tdtu.Proptech.repository.PropertyRepository;
import tdtu.Proptech.repository.RentalRepository;
import tdtu.Proptech.request.UploadRentedPropertyRequest;
import tdtu.Proptech.service.listing.ListingService;
import tdtu.Proptech.service.rental.RentalServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentalServiceTest {

    @Mock
    private ListingService listingService;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RentalServiceImpl rentalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPendingRentedProperties() {
        Rental rental = new Rental();
        when(rentalRepository.findByPropertyStatus("PENDING_RENTED"))
                .thenReturn(Collections.singletonList(rental));

        List<Rental> rentals = rentalService.getPendingRentedProperties();

        assertEquals(1, rentals.size());
        assertSame(rental, rentals.get(0));
        verify(rentalRepository, times(1)).findByPropertyStatus("PENDING_RENTED");
    }

    @Test
    void testGetRentedProperties() {
        Rental rental = new Rental();
        when(rentalRepository.findByPropertyStatus("RENTED"))
                .thenReturn(Collections.singletonList(rental));

        List<Rental> rentals = rentalService.getRentedProperties();

        assertEquals(1, rentals.size());
        assertSame(rental, rentals.get(0));
        verify(rentalRepository, times(1)).findByPropertyStatus("RENTED");
    }

    @Test
    void testUploadRentedProperty_Success() {
        String userEmail = "realtor@domain.com";
        UploadRentedPropertyRequest request = new UploadRentedPropertyRequest(
                1L,
                LocalDateTime.now(),
                12,
                1500.0,
                "Renter Name",
                "renter@domain.com",
                "123456789"
        );

        User realtor = new User();
        realtor.setEmail(userEmail);

        Property property = new Property();
        property.setId(1L);
        property.setRealtor(realtor);
        property.setStatus("AVAILABLE");

        Rental rental = new Rental();

        when(listingService.getPropertyById(1L)).thenReturn(property);
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        Rental result = rentalService.uploadRentedProperty(userEmail, request);

        assertNotNull(result);
        assertEquals("PENDING_RENTED", property.getStatus());
        verify(listingService, times(1)).getPropertyById(1L);
        verify(rentalRepository, times(1)).save(any(Rental.class));
    }

    @Test
    void testUploadRentedProperty_UnauthorizedAccess() {
        String userEmail = "realtor@domain.com";
        UploadRentedPropertyRequest request = new UploadRentedPropertyRequest(1L, LocalDateTime.now(), 12, 1500.0, "Renter", "renter@domain.com", "123456789");

        User otherRealtor = new User();
        otherRealtor.setEmail("other@domain.com");

        Property property = new Property();
        property.setId(1L);
        property.setRealtor(otherRealtor);

        when(listingService.getPropertyById(1L)).thenReturn(property);

        assertThrows(UnauthorizedAccessException.class,
                () -> rentalService.uploadRentedProperty(userEmail, request));
        verify(listingService, times(1)).getPropertyById(1L);
        verifyNoInteractions(rentalRepository);
    }

    @Test
    void testUpdatePendingRentedProperty_Rented() {
        Long propertyId = 1L;
        Property property = new Property();
        property.setId(propertyId);
        property.setStatus("PENDING_RENTED");

        Rental rental = new Rental();
        rental.setId(1L);

        when(listingService.getPropertyById(propertyId)).thenReturn(property);
        when(rentalRepository.findByPropertyId(propertyId)).thenReturn(Optional.of(rental));

        Rental result = rentalService.updatePendingRentedProperty(propertyId, "RENTED");

        assertNotNull(result);
        verify(listingService, times(1)).updatePendingProperty(propertyId, "RENTED");
        verify(rentalRepository, never()).deleteById(anyLong());
    }

    @Test
    void testUpdatePendingRentedProperty_Unavailable() {
        Long propertyId = 1L;
        Property property = new Property();
        property.setId(propertyId);
        property.setStatus("PENDING_RENTED");

        Rental rental = new Rental();
        rental.setId(1L);

        when(listingService.getPropertyById(propertyId)).thenReturn(property);
        when(rentalRepository.findByPropertyId(propertyId)).thenReturn(Optional.of(rental));

        Rental result = rentalService.updatePendingRentedProperty(propertyId, "UNAVAILABLE");

        assertNull(result);
        verify(listingService, times(1)).updatePendingProperty(propertyId, "UNAVAILABLE");
        verify(rentalRepository, times(1)).deleteById(rental.getId());
    }

    @Test
    void testGetRentalByPropertyId_Found() {
        Long propertyId = 1L;
        Rental rental = new Rental();
        when(rentalRepository.findByPropertyId(propertyId)).thenReturn(Optional.of(rental));

        Rental result = rentalService.getRentalByPropertyId(propertyId);

        assertNotNull(result);
        assertSame(rental, result);
        verify(rentalRepository, times(1)).findByPropertyId(propertyId);
    }

    @Test
    void testGetRentalByPropertyId_NotFound() {
        Long propertyId = 1L;
        when(rentalRepository.findByPropertyId(propertyId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> rentalService.getRentalByPropertyId(propertyId));
        assertEquals("Rental not found!", exception.getMessage());
        verify(rentalRepository, times(1)).findByPropertyId(propertyId);
    }

    @Test
    void testConvertRentalToRentalDTO() {
        Rental rental = new Rental();
        RentalDTO rentalDTO = new RentalDTO();

        when(modelMapper.map(rental, RentalDTO.class)).thenReturn(rentalDTO);

        RentalDTO result = rentalService.convertRentalToRentalDTO(rental);

        assertNotNull(result);
        assertSame(rentalDTO, result);
        verify(modelMapper, times(1)).map(rental, RentalDTO.class);
    }

    @Test
    void testConvertRentalsToRentalsDTO() {
        Rental rental = new Rental();
        RentalDTO rentalDTO = new RentalDTO();

        when(modelMapper.map(rental, RentalDTO.class)).thenReturn(rentalDTO);

        List<RentalDTO> result = rentalService.convertRentalsToRentalsDTO(Collections.singletonList(rental));

        assertEquals(1, result.size());
        assertSame(rentalDTO, result.get(0));
        verify(modelMapper, times(1)).map(rental, RentalDTO.class);
    }
}
