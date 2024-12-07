package tdtu.Proptech.dto;

import lombok.Data;
import tdtu.Proptech.model.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String imageURL;
    private Subscription subscription;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
