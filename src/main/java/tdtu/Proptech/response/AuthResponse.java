package tdtu.Proptech.response;

import lombok.Data;
import tdtu.Proptech.dto.UserDTO;

@Data
public class AuthResponse {
    private String token;
    private UserDTO user;

    public AuthResponse(String jwtToken, UserDTO user) {
        this.token = jwtToken;
        this.user = user;
    }
}