package microservices.book.multiplication.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.multiplication.serviceClients.GamificationServiceClient;
import microservices.book.multiplication.user.User;
import microservices.book.multiplication.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChallengeServiceImpl implements ChallengeService {

  private final UserRepository userRepository;
  private final ChallengeAttemptRepository attemptRepository;
  private final GamificationServiceClient gameClient;

  @Override
  public ChallengeAttempt verifyAttempt(ChallengeAttemptDTO attemptDTO) {
    // Check if the user already exists for that alias, otherwise create it
    User user =
        userRepository
            .findByAlias(attemptDTO.getUserAlias())
            .orElseGet(
                () -> {
                  log.info("Create new user with alias{}", attemptDTO.getUserAlias());
                  return userRepository.save(new User(attemptDTO.getUserAlias()));
                });

    // Check if the attempt is correct
    boolean isCorrect = attemptDTO.getGuess() == attemptDTO.getFactorA() * attemptDTO.getFactorB();

    // Builds the domain object. Null id since it'll be generated by the DB.
    ChallengeAttempt checkedAttempt =
        new ChallengeAttempt(
            null,
            user,
            attemptDTO.getFactorA(),
            attemptDTO.getFactorB(),
            attemptDTO.getGuess(),
            isCorrect);

    // Stores the attempt
    ChallengeAttempt storedAttempt = attemptRepository.save(checkedAttempt);

    // Sends the attempt to gamification and prints the response
    boolean status = gameClient.sendAttempt(storedAttempt);
    log.info("Gamification service response:{}", status);
    return storedAttempt;
  }

  @Override
  public List<ChallengeAttempt> getStatsForUser(final String alias) {
    return attemptRepository.findTop10ByUserAliasOrderByIdDesc(alias);
  }
}