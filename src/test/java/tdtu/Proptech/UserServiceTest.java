package tdtu.Proptech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import tdtu.Proptech.dto.UserDTO;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;
import tdtu.Proptech.repository.UserRepository;
import tdtu.Proptech.request.UserUpdateRequest;
import tdtu.Proptech.response.AuthResponse;
import tdtu.Proptech.security.jwt.JWTService;
import tdtu.Proptech.service.image.ImageService;
import tdtu.Proptech.service.subscription.SubscriptionService;
import tdtu.Proptech.service.user.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById_Found() {
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertSame(user, result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserById_NotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(userId));
        assertEquals("User not found!", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testUpdateUser_Success() {
        String userEmail = "user@domain.com";
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setEmail(userEmail);

        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("Updated Name");
        request.setPhone("123456789");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.updateUser(userEmail, userId, request);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("123456789", result.getPhone());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_UnauthorizedAccess() {
        String userEmail = "user@domain.com";
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setEmail("other@domain.com");

        UserUpdateRequest request = new UserUpdateRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        assertThrows(UnauthorizedAccessException.class, () -> userService.updateUser(userEmail, userId, request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_Success() {
        User user = new User();
        user.setEmail("user@domain.com");
        user.setPassword("password");

        Subscription starter = new Subscription();
        starter.setPlanName("STARTER");
        starter.setDurationInDays(30);

        when(subscriptionService.getSubscriptionsByPlanName("STARTER")).thenReturn(starter);
        when(userRepository.findByEmail("user@domain.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.register(user);

        assertNotNull(result);
        assertEquals("STARTER", result.getSubscription().getPlanName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        User user = new User();
        user.setEmail("user@domain.com");

        when(userRepository.findByEmail("user@domain.com")).thenReturn(Optional.of(new User()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.register(user));
        assertEquals("Email already exist", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void testChangePassword_Success() {
        String userEmail = "user@domain.com";
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setEmail(userEmail);
        existingUser.setPassword("encodedOldPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userService.changePassword(userEmail, userId, "oldPassword", "newPassword");

        assertEquals("encodedNewPassword", existingUser.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testChangePassword_UnauthorizedAccess() {
        String userEmail = "user@domain.com";
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setEmail("other@domain.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        assertThrows(UnauthorizedAccessException.class, () -> userService.changePassword(userEmail, userId, "oldPassword", "newPassword"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testConvertUserToDto() {
        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.convertUserToDto(user);

        assertNotNull(result);
        assertSame(userDTO, result);
        verify(modelMapper, times(1)).map(user, UserDTO.class);
    }
}
