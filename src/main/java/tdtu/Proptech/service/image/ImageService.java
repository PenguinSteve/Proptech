package tdtu.Proptech.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImg(MultipartFile file);
}
