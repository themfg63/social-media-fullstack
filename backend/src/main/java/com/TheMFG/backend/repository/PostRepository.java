package com.TheMFG.backend.repository;

import com.TheMFG.backend.model.Post;
import com.TheMFG.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Set<Post>> findByAuthor(User author);
}
