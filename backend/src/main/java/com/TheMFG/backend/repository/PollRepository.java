package com.TheMFG.backend.repository;

import com.TheMFG.backend.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll,Integer> {
}
