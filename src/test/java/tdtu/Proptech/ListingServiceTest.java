package tdtu.Proptech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import tdtu.Proptech.dto.PropertyDTO;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;
import tdtu.Proptech.repository.PropertyRepository;
import tdtu.Proptech.request.UploadPropertyRequest;
import tdtu.Proptech.service.image.ImageService;
import tdtu.Proptech.service.listing.ListingServiceImpl;
import tdtu.Proptech.service.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListingServiceTest {

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
	void testGetAllProperties() {
		Property property = new Property();
		property.setStatus("AVAILABLE");
		property.setExpire(LocalDateTime.now().plusDays(1));

		when(propertyRepository.findByStatusAndExpireAfter(eq("AVAILABLE"), any(LocalDateTime.class)))
				.thenReturn(Collections.singletonList(property));

		List<Property> properties = listingService.getAllProperties();

		assertEquals(1, properties.size());
		verify(propertyRepository, times(1)).findByStatusAndExpireAfter(eq("AVAILABLE"), any(LocalDateTime.class));
	}

	@Test
	void testUploadProperty_Success() {
		String userEmail = "test@domain.com";
		UploadPropertyRequest request = mock(UploadPropertyRequest.class);
		User user = new User();
		Subscription basic = new Subscription();
		basic.setId(2L);
		basic.setPlanName("BASIC");
		basic.setDurationInDays(30);
		basic.setPrice(600000.0);
		basic.setBenefits("Tối đa 10 tin (Hiển thị trong 14 ngày)");
		user.setSubscription(basic);
		user.setExpireSubscription(LocalDateTime.now().plusDays(basic.getDurationInDays()));

		Property newProperty = new Property();
		when(userService.getUserByEmail(userEmail)).thenReturn(user);
		when(propertyRepository.save(any(Property.class))).thenReturn(newProperty);
		when(imageService.saveImages(eq(newProperty), anyList())).thenReturn(Collections.emptyList());

		Property uploadedProperty = listingService.uploadProperty(userEmail, request);

		assertNotNull(uploadedProperty);
		verify(userService, times(1)).getUserByEmail(userEmail);
		verify(propertyRepository, times(2)).save(any(Property.class));
		verify(imageService, times(1)).saveImages(any(Property.class), anyList());
	}

	@Test
	void testUploadProperty_UserWithoutActiveSubscription() {
		String userEmail = "test@domain.com";
		UploadPropertyRequest request = mock(UploadPropertyRequest.class);
		User user = new User();
		user.setSubscription(null);

		when(userService.getUserByEmail(userEmail)).thenReturn(user);

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> listingService.uploadProperty(userEmail, request));

		assertEquals("User subscription is not active.", exception.getMessage());
		verify(userService, times(1)).getUserByEmail(userEmail);
	}

	@Test
	void testGetPropertyById_NotFound() {
		when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> listingService.getPropertyById(1L));

		assertEquals("Property not found!", exception.getMessage());
	}

	@Test
	void testModifyProperty_Unauthorized() {
		String userEmail = "unauthorized@domain.com";
		Long propertyId = 1L;
		UploadPropertyRequest request = mock(UploadPropertyRequest.class);

		Property property = new Property();
		User realtor = new User();
		realtor.setEmail("authorized@domain.com");
		property.setRealtor(realtor);

		when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));

		UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class,
				() -> listingService.modifyProperty(userEmail, propertyId, request));

		assertEquals("You are not authorized to update this property.", exception.getMessage());
	}

	@Test
	void testGetPropertiesByCriteria() {
		Property property = new Property();
		property.setStatus("AVAILABLE");
		property.setExpire(LocalDateTime.now().plusDays(1));

		when(propertyRepository.findPropertiesByCriteria(eq("SALES"), eq(100.0), eq(500.0), eq("House"), eq("NY")))
				.thenReturn(Collections.singletonList(property));

		List<Property> properties = listingService.getPropertiesByCriteria("SALES", 100.0, 500.0, "House", "NY");

		assertEquals(1, properties.size());
		verify(propertyRepository, times(1)).findPropertiesByCriteria("SALES", 100.0, 500.0, "House", "NY");
	}

	@Test
	void testConvertPropertiesToDTO() {
		Property property = new Property();
		PropertyDTO propertyDTO = new PropertyDTO();

		when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);

		List<PropertyDTO> result = listingService.convertPropetiesToPropertiesDTO(Collections.singletonList(property));

		assertEquals(1, result.size());
		assertSame(propertyDTO, result.get(0));
		verify(modelMapper, times(1)).map(property, PropertyDTO.class);
	}
}