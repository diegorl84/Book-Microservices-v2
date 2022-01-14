package microservices.book.multiplication.challenge;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ChallengeGeneratorServiceImplTest {

    private ChallengeGeneratorService challengeGeneratorService;

    @Spy
    private Random random;

    @BeforeEach
    public void setup(){
        challengeGeneratorService = new ChallengeGeneratorServiceImpl(random);
    }

    @Test
    void generateRandomFactorIsBetweenExpectedLimits() {
        // Given:
        given(random.nextInt(89)).willReturn(20,30);

        // When: we generate a challenge
        Challenge challenge = challengeGeneratorService.randomChallenge();

        // Then: the challenge contains factors as expected
        BDDAssertions.then(challenge).isEqualTo(new Challenge(31,41));

    }
}