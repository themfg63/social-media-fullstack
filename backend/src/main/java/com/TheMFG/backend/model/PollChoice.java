package com.TheMFG.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "poll_choices")
public class PollChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pollChoiceId;

    @ManyToOne
    @JoinColumn(name = "poll_id")
    @JsonIgnore
    private Poll poll;

    @Column(name = "poll_choice_text")
    private String choiceText;

    @OneToMany
    Set<User> votes;

    @Override
    public String toString() {
        return "PollChoice{" +
                "pollChoiceId=" + pollChoiceId +
                ", poll=" + poll +
                ", choiceText='" + choiceText + '\'' +
                ", votes=" + votes +
                '}';
    }
}
