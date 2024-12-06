package tdtu.Proptech.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;
import tdtu.Proptech.repository.UserRepository;
import tdtu.Proptech.response.AuthResponse;
import tdtu.Proptech.security.jwt.JWTService;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    @Override
    public User register(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Email already exist");
        }
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public AuthResponse verify(User user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if(authentication.isAuthenticated()) {
            user = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
            String jwtToken = jwtService.generateToken(user.getEmail());

            String role = authentication.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("USER");
            return new AuthResponse(jwtToken, user);
        }
        return null;
    }

    public User assignSubscription(User user, Subscription subscription) {
        user.setSubscription(subscription);
        LocalDateTime startDate = LocalDateTime.now();
        user.setStartDate(startDate);

        LocalDateTime endDate = startDate.plusDays(subscription.getDurationInDays());
        user.setEndDate(endDate);

        return userRepository.save(user);
    }
}