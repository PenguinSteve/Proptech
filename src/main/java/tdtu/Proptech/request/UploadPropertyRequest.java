package tdtu.Proptech.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadPropertyRequest {
    private String name;
    private String address;
    private Double price;
    private String area;
    private String description;
    private String type;
    private List<MultipartFile> images;
}
