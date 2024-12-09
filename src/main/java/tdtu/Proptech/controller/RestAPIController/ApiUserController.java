package tdtu.Proptech.controller.RestAPIController;

import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tdtu.Proptech.dto.UserDTO;
import tdtu.Proptech.exceptions.UnauthorizedAccessException;
import tdtu.Proptech.model.User;
import tdtu.Proptech.request.ChangePasswordRequest;
import tdtu.Proptech.request.LoginRequest;
import tdtu.Proptech.request.RegisterRequest;
import tdtu.Proptech.request.UserUpdateRequest;
import tdtu.Proptech.response.ApiResponse;
import tdtu.Proptech.service.user.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class ApiUserController {

	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
		try {
			User user = new User();
			user.setName(request.getName());
			user.setEmail(request.getEmail());
			user.setPhone(request.getPhone());
			user.setPassword(request.getPassword());
			User registeredUser = userService.register(user);
			UserDTO userDTO = userService.convertUserToDto(registeredUser);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ApiResponse("User registered successfully", userDTO));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
		try {
			User user = new User();
			user.setPassword(request.getPassword());
			user.setEmail(request.getEmail());
			return ResponseEntity.ok(new ApiResponse("Login successfully", userService.verify(user)));
		} catch (JwtException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentEmail = authentication.getName();

		try {
			return ResponseEntity.ok(new ApiResponse("User information updated successfully.",
					userService.convertUserToDto(userService.updateUser(currentEmail, id, request))));
		} catch (UnauthorizedAccessException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage(), null));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}

	}

	@PutMapping("/{userId}/change-password")
	public ResponseEntity<ApiResponse> changePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest request) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentUserEmail = authentication.getName();

			userService.changePassword(currentUserEmail, userId, request.getOldPassword(), request.getNewPassword());
			return ResponseEntity.ok(new ApiResponse("Password changed successfully", null));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
		}
	}
}
