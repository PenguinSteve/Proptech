package tdtu.Proptech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdtu.Proptech.model.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String imageURL;
    private Subscription subscription;
    private LocalDateTime expireSubscription;
}
