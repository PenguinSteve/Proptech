package tdtu.Proptech.service.image;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tdtu.Proptech.model.Image;
import tdtu.Proptech.model.Property;
import tdtu.Proptech.repository.ImageRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    private final ImageRepository imageRepository;

    @Override
    public String uploadImg(MultipartFile file) {
        try{
            Map data = cloudinary.uploader().upload(file.getBytes(), Map.of());
            return (String) data.get("url");
        }
        catch (IOException e){
            throw new RuntimeException("Image uploading fail");
        }
    }

    @Override
    public List<Image> saveImages(Property property, List<MultipartFile> images) {
        List<Image> savedImages = new ArrayList<>();
        for(MultipartFile image : images){
            Image newImage = new Image();
            newImage.setImageURL(uploadImg(image));
            newImage.setProperty(property);
            Image savedImage =  imageRepository.save(newImage);
            savedImages.add(savedImage);
        }
        return savedImages;
    }

    @Override
    public void deleteImage(Image image) {
        imageRepository.delete(image);
    }
}