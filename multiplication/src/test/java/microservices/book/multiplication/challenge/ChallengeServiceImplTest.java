package microservices.book.multiplication.challenge;

import microservices.book.multiplication.user.User;
import microservices.book.multiplication.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceImplTest {

  private ChallengeService challengeService;

  @Mock private UserRepository userRepository;

  @Mock private ChallengeAttemptRepository attemptRepository;

  //@Mock private GamificationServiceClient gameClient;
  @Mock private ChallengeEventPub challengeEventPub;

  @BeforeEach
  public void setUp() {
    challengeService = new ChallengeServiceImpl(userRepository, attemptRepository, challengeEventPub);
  }

  @Test
  void checkCorrectAttemptTest() {
    // Given
    given(attemptRepository.save(any())).will(returnsFirstArg());

    ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 3000);
    // When
    ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

    // Then
    then(resultAttempt.isCorrect()).isTrue();
    verify(userRepository).save(new User("john_doe"));
    verify(attemptRepository).save(resultAttempt);
    verify(challengeEventPub).challegeSolved(resultAttempt);
  }

  @Test
  void checkWrongAttemptTest() {
    // Given
    given(attemptRepository.save(any())).will(returnsFirstArg());

    ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 5000);
    // When
    ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

    // Then
    then(resultAttempt.isCorrect()).isFalse();
    verify(userRepository).save(new User("john_doe"));
    verify(attemptRepository).save(resultAttempt);
  }

  @Test
  void checkExistingUserTest() {
    // Given
    given(attemptRepository.save(any())).will(returnsFirstArg());

    User exitingUser = new User(1L, "john_doe");
    given(userRepository.findByAlias("john_doe")).willReturn(Optional.of(exitingUser));

    ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 5000);

    // When
    ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

    // then
    then(resultAttempt.isCorrect()).isFalse();
    then(resultAttempt.getUser()).isEqualTo(exitingUser);
    verify(userRepository, never()).save(any());
    verify(attemptRepository).save(resultAttempt);
  }

  @Test
  void checkRetrieveLastAttemptsTest() {
    // Given
    User user = new User("john_doe");
    ChallengeAttempt attempt1 = new ChallengeAttempt(1L, user, 50, 60, 3010, false);
    ChallengeAttempt attempt2 = new ChallengeAttempt(1L, user, 50, 60, 3020, false);
    List<ChallengeAttempt> lastAttempts = List.of(attempt1, attempt2);
    given(attemptRepository.findTop10ByUserAliasOrderByIdDesc("john_doe")).willReturn(lastAttempts);

    // when
    List<ChallengeAttempt> lastAttemptsResult = challengeService.getStatsForUser("john_doe");

    // then
    then(lastAttemptsResult).isEqualTo(lastAttempts);
  }
}
