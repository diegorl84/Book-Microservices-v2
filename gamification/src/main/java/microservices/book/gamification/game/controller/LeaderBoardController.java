package microservices.book.gamification.game.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.game.LeaderBoardService;
import microservices.book.gamification.game.domain.LeaderBoardRow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class implements a REST API for the Gamification LeaderBoard service.
 */
@Slf4j
@RestController
@RequestMapping("/leaders")
@RequiredArgsConstructor
public class LeaderBoardController {
    private final LeaderBoardService leaderBoardService;

    @GetMapping
    private List<LeaderBoardRow> getLeaderBoard(){
        log.info("Retrieving leaderboard");
        return leaderBoardService.getCurrentLeaderBoard();

    }
}
