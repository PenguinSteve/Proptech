package tdtu.Proptech.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateRequest {
    private String name;
    private String phone;
    private MultipartFile imageURL;
}
