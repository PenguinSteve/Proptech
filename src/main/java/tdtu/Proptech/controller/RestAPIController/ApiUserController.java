package tdtu.Proptech.controller.RestAPIController;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tdtu.Proptech.model.User;
import tdtu.Proptech.response.ApiResponse;
import tdtu.Proptech.service.user.UserService;

@RestController
@RequestMapping("/api/user")
public class ApiUserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody User user){
        try {
            User registeredUser = userService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("User registered successfully", registeredUser));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody User user){
        try{
            return ResponseEntity.ok(new ApiResponse("Login successfully", userService.verify(user)));
        }
        catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
