package com.example.footballchampionship;


import com.example.footballchampionship.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Transactional
    public List<Match> addMatches(List<Match> matches) {
        return matchRepository.saveAll(matches);
    }

    public Match getMatch(MatchId id) throws ResourceNotFoundException {
        return matchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Match not found"));
    }

    public boolean existsById(MatchId id) {
        return matchRepository.existsById(id);
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public List<Match> updateMatch(List<Match> matches) {
        return matchRepository.saveAll(matches);
    }

    public void clearData() {
        matchRepository.deleteAll();
    }
}
