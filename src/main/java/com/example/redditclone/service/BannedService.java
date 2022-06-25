package com.example.redditclone.service;

import com.example.redditclone.models.Banned;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BannedService {

    Optional<Banned> one(Long id);

    List<Banned> all();

    Banned save(Banned banned);

    void delete(Long id);
}
