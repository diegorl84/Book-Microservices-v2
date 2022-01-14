package microservices.book.gamification.game;

import microservices.book.gamification.game.domain.BadgeCard;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.LeaderBoardRow;
import microservices.book.gamification.game.repository.BadgeRepository;
import microservices.book.gamification.game.repository.ScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LeaderBoardServiceImplTest {

  private LeaderBoardService leaderBoardService;

  @Mock private ScoreRepository scoreRepository;

  @Mock private BadgeRepository badgeRepository;

  @BeforeEach
  void setUp() {
    leaderBoardService = new LeaderBoardServiceImpl(scoreRepository, badgeRepository);
  }

  @Test
  void getCurrentLeaderBoard() {
    // given
    LeaderBoardRow leaderBoardRow = new LeaderBoardRow(1L, 300L, List.of());
    given(scoreRepository.findFirst10()).willReturn(List.of(leaderBoardRow));
    given(badgeRepository.findByUserIdOrderByBadgeTimestampDesc(1L))
        .willReturn(List.of(new BadgeCard(1L, BadgeType.LUCKY_NUMBER)));

    // When
    List<LeaderBoardRow> leaderBoardRowList = leaderBoardService.getCurrentLeaderBoard();

    // then
    List<LeaderBoardRow> expected =
        List.of(new LeaderBoardRow(1l, 300L, List.of(BadgeType.LUCKY_NUMBER.getDescription())));
    then(leaderBoardRowList).isEqualTo(expected);
  }
}
