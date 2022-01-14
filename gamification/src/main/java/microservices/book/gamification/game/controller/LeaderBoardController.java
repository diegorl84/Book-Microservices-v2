package microservices.book.gamification.game.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import microservices.book.gamification.game.LeaderBoardService;
import microservices.book.gamification.game.domain.LeaderBoardRow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class implements a REST API for the Gamification LeaderBoard service.
 */
@RestController
@RequestMapping("/leaders")
@RequiredArgsConstructor
public class LeaderBoardController {
    private final LeaderBoardService leaderBoardService;

    @GetMapping
    private List<LeaderBoardRow> getLeaderBoard(){
        return leaderBoardService.getCurrentLeaderBoard();

    }
}
