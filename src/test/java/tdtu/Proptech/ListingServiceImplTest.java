package tdtu.Proptech;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import tdtu.Proptech.dto.PropertyDTO;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.User;
import tdtu.Proptech.repository.PropertyRepository;
import tdtu.Proptech.request.UploadPropertyRequest;
import tdtu.Proptech.service.image.ImageService;
import tdtu.Proptech.service.listing.ListingServiceImpl;
import tdtu.Proptech.service.user.UserService;

class ListingServiceImplTest {

	@Mock
	private PropertyRepository propertyRepository;

	@Mock
	private UserService userService;

	@Mock
	private ImageService imageService;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private ListingServiceImpl listingService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}



	@Test
	void testGetPropertyById_Found() {
		Property property = new Property();
		when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

		Property result = listingService.getPropertyById(1L);

		assertNotNull(result);
		assertEquals(property, result);
		verify(propertyRepository, times(1)).findById(1L);
	}

	@Test
	void testGetPropertyById_NotFound() {
		when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> listingService.getPropertyById(1L));

		assertEquals("Property not found!", exception.getMessage());
	}

//    @Test
//    void testUploadProperty_Success() {
//        User user = new User();
//        user.setSubscriptionActive(true);
//        user.setSubscription(new User.Subscription("STARTER"));
//
//        UploadPropertyRequest request = mock(UploadPropertyRequest.class);
//        Property property = new Property();
//        property.setImages(new ArrayList<>());
//
//        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
//        when(propertyRepository.save(any(Property.class))).thenReturn(property);
//        when(request.getImages()).thenReturn(Collections.emptyList());
//
//        Property result = listingService.uploadProperty("test@example.com", request);
//
//        assertNotNull(result);
//        verify(userService, times(1)).getUserByEmail("test@example.com");
//        verify(propertyRepository, times(2)).save(any(Property.class));
//    }

	@Test
	void testModifyProperty_UnauthorizedAccess() {
		Property property = new Property();
		User realtor = new User();
		realtor.setEmail("different@example.com");
		property.setRealtor(realtor);

		when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

		UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class,
				() -> listingService.modifyProperty("test@example.com", 1L, mock(UploadPropertyRequest.class)));

		assertEquals("You are not authorized to update this property.", exception.getMessage());
	}

	@Test
	void testConvertPropertiesToPropertiesDTO() {
		Property property = new Property();
		PropertyDTO propertyDTO = new PropertyDTO();
		when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);

		List<PropertyDTO> result = listingService.convertPropetiesToPropertiesDTO(List.of(property));

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(propertyDTO, result.get(0));
		verify(modelMapper, times(1)).map(property, PropertyDTO.class);
	}
}
