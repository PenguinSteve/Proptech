package tdtu.Proptech.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadPropertyRequest {
    private String name;
    private String address;
    private Double price;
    private String area;
    private String description;
    private String type;
    private List<MultipartFile> images;
}
