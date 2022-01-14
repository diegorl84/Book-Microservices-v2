package microservices.book.multiplication.challenge.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.multiplication.challenge.ChallengeAttempt;
import microservices.book.multiplication.challenge.ChallengeAttemptDTO;
import microservices.book.multiplication.challenge.ChallengeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/** This class provides a REST API to POST the attempts from users. */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/attempts")
public class ChallengeAttemptController {

  private final ChallengeService challengeService;

  @PostMapping
  ResponseEntity<ChallengeAttempt> postResult(
      @RequestBody @Valid ChallengeAttemptDTO challengeAttemptDTO) {
    return ResponseEntity.ok(challengeService.verifyAttempt(challengeAttemptDTO));
  }

  @GetMapping
  ResponseEntity<List<ChallengeAttempt>> getStatistics(@RequestParam("alias") String alias) {
    return ResponseEntity.ok(challengeService.getStatsForUser(alias));
  }
}