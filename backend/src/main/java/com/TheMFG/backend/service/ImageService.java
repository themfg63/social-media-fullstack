package com.TheMFG.backend.service;

import com.TheMFG.backend.exception.UnableToResolvePhotoException;
import com.TheMFG.backend.exception.UnableToSavePhotoException;
import com.TheMFG.backend.model.Image;
import com.TheMFG.backend.repository.ImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@Transactional
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    private static final File DIRECTORY = new File("C:\\Users\\Asus\\Desktop\\SpringBoot\\social-media-fullstack\\backend\\img");
    private static final String URL = "http://localhost:8081/images";

    public Image saveGifFromPost(Image image){
        return imageRepository.save(image);
    }

    public Image uploadImage(MultipartFile file, String prefix) throws UnableToSavePhotoException {
        try{
            String extention = "." + file.getContentType().split("/")[1];
            File img = File.createTempFile(prefix,extention,DIRECTORY);
            file.transferTo(img);
            String imageURL = URL + img.getName();
            Image i = new Image(img.getName(), file.getContentType(),img.getPath(),imageURL);
            Image saved = imageRepository.save(i);
            return saved;
        }catch (IOException e){
            throw new UnableToSavePhotoException();
        }
    }

    public byte[] downloadImage(String filename) throws UnableToResolvePhotoException {
        try{
            Image image = imageRepository.findByImageName(filename).get();
            String filePath = image.getImagePath();
            byte[] imageBytes = Files.readAllBytes(new File(filePath).toPath());
            return imageBytes;
        }catch (IOException e){
            throw new UnableToResolvePhotoException();
        }
    }

    public String getImageType(String filename){
        Image image = imageRepository.findByImageName(filename).get();
        return image.getImageType();
    }
}
