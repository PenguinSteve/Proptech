package tdtu.Proptech.service.user;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{


    private final UserRepository userRepository;

    private final SubscriptionService subscriptionService;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final ImageService imageService;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    @Override
    public User updateUser(String userEmail, Long id, UserUpdateRequest request) {
        User existingUser = getUserById(id);

        if(!userEmail.equals(existingUser.getEmail())){
            throw new UnauthorizedAccessException("You are not authorized to update this user's information.");
        }

        User updatedUser = updateExistingUser(existingUser, request);

        return userRepository.save(updatedUser);
    }

    private User updateExistingUser(User existingUser, UserUpdateRequest request){
        existingUser.setName(request.getName());
        existingUser.setPhone(request.getPhone());
        if (request.getImageURL() != null && !request.getImageURL().isEmpty()) {
            existingUser.setImageURL(imageService.uploadImg(request.getImageURL()));
        }
        return existingUser;
    }

    @Override
    public User register(User user) {
        Subscription starter = subscriptionService.getSubscriptionsByPlanName("STARTER");
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Email already exist");
        }
        user.setImageURL("https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=612x612&w=0&k=20&c=yBeyba0hUkh14_jgv1OKqIH0CCSWU_4ckRkAoy2p73o=");
        user.setRole("REALTOR");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setSubscription(starter);
        user.setExpireSubscription(LocalDateTime.now().plusDays(starter.getDurationInDays()));
        return userRepository.save(user);
    }

    @Override
    public AuthResponse verify(User user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if(authentication.isAuthenticated()) {
            user = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
            String jwtToken = jwtService.generateToken(user.getEmail());

            String role = authentication.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("REALTOR");
            return new AuthResponse(jwtToken, convertUserToDto(user));
        }
        return null;
    }


    @Override
    public UserDTO convertUserToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!"));
    }
}