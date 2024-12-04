package tdtu.Proptech.service.user;

import tdtu.SpringCommerce.model.User;
import tdtu.SpringCommerce.response.AuthResponse;

public interface UserService {
    User register(User user);
    AuthResponse verify(User user);
    User getUserById(Long userId);
}
