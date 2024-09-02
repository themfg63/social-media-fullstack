package com.TheMFG.backend.repository;

import com.TheMFG.backend.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {
    Optional<Image> findByImageName(String imageName);
}
