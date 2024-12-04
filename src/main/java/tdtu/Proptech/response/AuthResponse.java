package tdtu.Proptech.response;

import lombok.Data;
import tdtu.SpringCommerce.model.User;

@Data
public class AuthResponse {
    private String token;
    private User user;

    public AuthResponse(String jwtToken, User user) {
        this.token = jwtToken;
        this.user = user;
    }
}