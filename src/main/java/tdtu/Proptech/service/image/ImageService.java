package tdtu.Proptech.service.image;

import org.springframework.web.multipart.MultipartFile;
import tdtu.Proptech.model.Image;
import tdtu.Proptech.model.Property;

import java.util.List;

public interface ImageService {
    String uploadImg(MultipartFile file);

    List<Image> saveImages(Property property, List<MultipartFile> images);

    void deleteImage(Image image);
}
