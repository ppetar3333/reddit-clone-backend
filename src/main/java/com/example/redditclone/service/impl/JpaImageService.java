package com.example.redditclone.service.impl;

import com.example.redditclone.models.Image;
import com.example.redditclone.repository.ImageRepository;
import com.example.redditclone.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaImageService implements ImageService {

    @Autowired
    private ImageRepository imageRepository;


    @Override
    public void save(Image image) {
        imageRepository.save(image);
    }

    @Override
    public Optional<Image> findByName(String name) {
        return imageRepository.findByName(name);
    }
}
