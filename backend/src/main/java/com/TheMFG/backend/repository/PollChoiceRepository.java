package com.TheMFG.backend.repository;

import com.TheMFG.backend.model.PollChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollChoiceRepository extends JpaRepository<PollChoice,Integer> {
}
