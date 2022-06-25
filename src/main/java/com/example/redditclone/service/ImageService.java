package com.example.redditclone.service;

import com.example.redditclone.models.Image;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ImageService {
    void save(Image image);

    Optional<Image> findByName(String name);
}
