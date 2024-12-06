package tdtu.Proptech.service.user;

import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;
import tdtu.Proptech.response.AuthResponse;

public interface UserService {
    User register(User user);
    AuthResponse verify(User user);
    User getUserById(Long userId);
    User assignSubscription(User user, Subscription subscription);
}
