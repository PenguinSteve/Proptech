package tdtu.Proptech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import tdtu.Proptech.dto.SalesDTO;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.Sales;
import tdtu.Proptech.model.User;
import tdtu.Proptech.repository.SalesRepository;
import tdtu.Proptech.request.UploadSoldPropertyRequest;
import tdtu.Proptech.service.listing.ListingService;
import tdtu.Proptech.service.sales.SalesServiceImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalesServiceTest {

    @Mock
    private ListingService listingService;

    @Mock
    private SalesRepository salesRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SalesServiceImpl salesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPendingSoldProperties() {
        Sales sales = new Sales();
        when(salesRepository.findByPropertyStatus("PENDING_SOLD"))
                .thenReturn(Collections.singletonList(sales));

        List<Sales> result = salesService.getPendingSoldProperties();

        assertEquals(1, result.size());
        assertSame(sales, result.get(0));
        verify(salesRepository, times(1)).findByPropertyStatus("PENDING_SOLD");
    }

    @Test
    void testGetSoldProperties() {
        Sales sales = new Sales();
        when(salesRepository.findByPropertyStatus("SOLD"))
                .thenReturn(Collections.singletonList(sales));

        List<Sales> result = salesService.getSoldProperties();

        assertEquals(1, result.size());
        assertSame(sales, result.get(0));
        verify(salesRepository, times(1)).findByPropertyStatus("SOLD");
    }

    @Test
    void testUploadSoldProperty_Success() {
        String userEmail = "realtor@domain.com";
        UploadSoldPropertyRequest request = new UploadSoldPropertyRequest(
                1L,
                LocalDate.now(),
                500000.0,
                "Buyer Name",
                "buyer@domain.com",
                "123456789"
        );

        User realtor = new User();
        realtor.setEmail(userEmail);

        Property property = new Property();
        property.setId(1L);
        property.setRealtor(realtor);
        property.setStatus("AVAILABLE");

        Sales sales = new Sales();

        when(listingService.getPropertyById(1L)).thenReturn(property);
        when(salesRepository.save(any(Sales.class))).thenReturn(sales);

        Sales result = salesService.uploadSoldProperty(userEmail, request);

        assertNotNull(result);
        assertEquals("PENDING_SOLD", property.getStatus());
        verify(listingService, times(1)).getPropertyById(1L);
        verify(salesRepository, times(1)).save(any(Sales.class));
    }

    @Test
    void testUploadSoldProperty_UnauthorizedAccess() {
        String userEmail = "realtor@domain.com";
        UploadSoldPropertyRequest request = new UploadSoldPropertyRequest(
                1L, LocalDate.now(), 500000.0, "Buyer", "buyer@domain.com", "123456789"
        );

        User otherRealtor = new User();
        otherRealtor.setEmail("other@domain.com");

        Property property = new Property();
        property.setId(1L);
        property.setRealtor(otherRealtor);

        when(listingService.getPropertyById(1L)).thenReturn(property);

        assertThrows(UnauthorizedAccessException.class,
                () -> salesService.uploadSoldProperty(userEmail, request));
        verify(listingService, times(1)).getPropertyById(1L);
        verifyNoInteractions(salesRepository);
    }

    @Test
    void testUpdatePendingSoldProperty_Sold() {
        Long propertyId = 1L;
        Property property = new Property();
        property.setId(propertyId);
        property.setStatus("PENDING_SOLD");

        Sales sales = new Sales();
        sales.setId(1L);

        when(listingService.getPropertyById(propertyId)).thenReturn(property);
        when(salesRepository.findByPropertyId(propertyId)).thenReturn(Optional.of(sales));

        Sales result = salesService.updatePendingSoldProperty(propertyId, "SOLD");

        assertNotNull(result);
        verify(listingService, times(1)).updatePendingProperty(propertyId, "SOLD");
        verify(salesRepository, never()).deleteById(anyLong());
    }

    @Test
    void testUpdatePendingSoldProperty_Unavailable() {
        Long propertyId = 1L;
        Property property = new Property();
        property.setId(propertyId);
        property.setStatus("PENDING_SOLD");

        Sales sales = new Sales();
        sales.setId(1L);

        when(listingService.getPropertyById(propertyId)).thenReturn(property);
        when(salesRepository.findByPropertyId(propertyId)).thenReturn(Optional.of(sales));

        Sales result = salesService.updatePendingSoldProperty(propertyId, "UNAVAILABLE");

        assertNull(result);
        verify(listingService, times(1)).updatePendingProperty(propertyId, "UNAVAILABLE");
        verify(salesRepository, times(1)).deleteById(sales.getId());
    }

    @Test
    void testGetSalesByPropertyId_Found() {
        Long propertyId = 1L;
        Sales sales = new Sales();
        when(salesRepository.findByPropertyId(propertyId)).thenReturn(Optional.of(sales));

        Sales result = salesService.getSalesByPropertyId(propertyId);

        assertNotNull(result);
        assertSame(sales, result);
        verify(salesRepository, times(1)).findByPropertyId(propertyId);
    }

    @Test
    void testGetSalesByPropertyId_NotFound() {
        Long propertyId = 1L;
        when(salesRepository.findByPropertyId(propertyId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> salesService.getSalesByPropertyId(propertyId));
        assertEquals("Sales not found!", exception.getMessage());
        verify(salesRepository, times(1)).findByPropertyId(propertyId);
    }

    @Test
    void testConvertSalesToSalesDTO() {
        Sales sales = new Sales();
        SalesDTO salesDTO = new SalesDTO();

        when(modelMapper.map(sales, SalesDTO.class)).thenReturn(salesDTO);

        SalesDTO result = salesService.convertSalesToSalesDTO(sales);

        assertNotNull(result);
        assertSame(salesDTO, result);
        verify(modelMapper, times(1)).map(sales, SalesDTO.class);
    }

    @Test
    void testConvertListSalesToListSalesDTO() {
        Sales sales = new Sales();
        SalesDTO salesDTO = new SalesDTO();

        when(modelMapper.map(sales, SalesDTO.class)).thenReturn(salesDTO);

        List<SalesDTO> result = salesService.convertListSalesToListSalesDTO(Collections.singletonList(sales));

        assertEquals(1, result.size());
        assertSame(salesDTO, result.get(0));
        verify(modelMapper, times(1)).map(sales, SalesDTO.class);
    }
}
