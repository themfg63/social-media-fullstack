package com.TheMFG.backend.service;

import com.TheMFG.backend.model.Poll;
import com.TheMFG.backend.model.PollChoice;
import com.TheMFG.backend.model.User;
import com.TheMFG.backend.repository.PollChoiceRepository;
import com.TheMFG.backend.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollService {
    private final PollRepository pollRepository;
    private final PollChoiceRepository pollChoiceRepository;
    private final UserService userService;

    public PollChoice generateChoice(PollChoice choice){
        return pollChoiceRepository.save(choice);
    }

    public Poll generatePoll(Poll poll){
        return pollRepository.save(poll);
    }

    public Poll voteForChoice(Integer choiceId, Integer userId){
        User user = userService.getUserById(userId);

        PollChoice choice = pollChoiceRepository.findById(choiceId).orElseThrow();
        Poll poll = choice.getPoll();

        List<User> votes = new ArrayList<>();
        poll.getChoices()
                .forEach(choice1 -> {
                    choice1.getVotes()
                            .forEach(voteUser -> {
                                votes.add(voteUser);
                            });
                });

        if(votes.contains(user)){
            return poll;
        }

        Set<User> currentVotes = choice.getVotes();
        currentVotes.add(user);
        choice.setVotes(currentVotes);
        pollChoiceRepository.save(choice);

        List<PollChoice> choiceList = poll.getChoices();
        choiceList.set(poll.getChoices().indexOf(choice),choice);
        return pollRepository.save(poll);
    }


}
