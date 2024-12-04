package tdtu.Proptech.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tdtu.SpringCommerce.model.User;
import tdtu.SpringCommerce.repository.UserRepository;
import tdtu.SpringCommerce.response.AuthResponse;
import tdtu.SpringCommerce.security.jwt.JWTService;

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
}