package tdtu.Proptech.service.image;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private Cloudinary cloudinary;

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
}
