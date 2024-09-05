package com.TheMFG.backend.model;

import com.TheMFG.backend.config.CustomLocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "polls")
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_id")
    private Integer pollId;

    @Column(name = "end_date")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "poll")
    private List<PollChoice> choices;

    @Override
    public String toString() {
        return "Poll{" +
                "pollId=" + pollId +
                ", endTime=" + endTime +
                ", choices=" + choices +
                '}';
    }
}
