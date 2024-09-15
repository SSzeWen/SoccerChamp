package com.example.footballchampionship;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/matches")
    public ResponseEntity<List<Match>> addMatches(@RequestBody List<Match> matches) {
        List<Match> savedMatches = matchService.addMatches(matches);
        if (savedMatches != null && !savedMatches.isEmpty()) {
            return new ResponseEntity<>(savedMatches, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}