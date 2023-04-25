package com.example.redditclone.repository.jpa;

import com.example.redditclone.models.Banned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannedRepository extends JpaRepository<Banned, Long> {
}
