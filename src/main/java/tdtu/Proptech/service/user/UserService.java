package tdtu.Proptech.service.user;

import tdtu.Proptech.dto.UserDTO;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;
import tdtu.Proptech.request.UserUpdateRequest;
import tdtu.Proptech.response.AuthResponse;

public interface UserService {
    User register(User user);
    AuthResponse verify(User user);
    User getUserById(Long userId);
    User updateUser(String userEmail, Long id, UserUpdateRequest request);
    UserDTO convertUserToDto(User user);
    User getUserByEmail(String email);
}
