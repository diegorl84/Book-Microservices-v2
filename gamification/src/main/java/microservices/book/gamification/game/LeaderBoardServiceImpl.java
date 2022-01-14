package microservices.book.gamification.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.game.domain.LeaderBoardRow;
import microservices.book.gamification.game.repository.BadgeRepository;
import microservices.book.gamification.game.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderBoardServiceImpl implements LeaderBoardService {

  private final ScoreRepository scoreRepository;
  private final BadgeRepository badgeRepository;

  @Override
  public List<LeaderBoardRow> getCurrentLeaderBoard() {
    List<LeaderBoardRow> scoreOnly = scoreRepository.findFirst10();
    return scoreOnly.stream()
        .map(
            row -> {
              List<String> badges =
                  badgeRepository.findByUserIdOrderByBadgeTimestampDesc(row.getUserId()).stream()
                      .map(b -> b.getBadgeType().getDescription())
                      .collect(Collectors.toList());
              return row.withBadges(badges);
            })
        .collect(Collectors.toList());
  }
}
