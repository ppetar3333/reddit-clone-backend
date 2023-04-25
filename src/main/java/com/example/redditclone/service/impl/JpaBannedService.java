package com.example.redditclone.service.impl;

import com.example.redditclone.models.Banned;
import com.example.redditclone.repository.jpa.BannedRepository;
import com.example.redditclone.service.BannedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JpaBannedService implements BannedService {

    @Autowired
    private BannedRepository bannedRepository;

    @Override
    public Optional<Banned> one(Long id) {
        return bannedRepository.findById(id);
    }

    @Override
    public List<Banned> all() {
        return bannedRepository.findAll();
    }

    @Override
    public Banned save(Banned banned) {
        return bannedRepository.save(banned);
    }

    @Override
    public void delete(Long id) {
        bannedRepository.deleteById(id);
    }
}
