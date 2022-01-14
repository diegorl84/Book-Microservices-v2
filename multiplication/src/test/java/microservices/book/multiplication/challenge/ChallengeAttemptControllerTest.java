package microservices.book.multiplication.challenge;

import microservices.book.multiplication.challenge.controller.ChallengeAttemptController;
import microservices.book.multiplication.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(ChallengeAttemptController.class)
class ChallengeAttemptControllerTest {

  @MockBean private ChallengeService challengeService;

  @Autowired private MockMvc mvc;

  @Autowired private JacksonTester<ChallengeAttemptDTO> jsonRequestAttempt;

  @Autowired private JacksonTester<ChallengeAttempt> jsonResultAttempt;

  @BeforeEach
  void setUp() {}

  @Test
  void postValidResult() throws Exception {
    // Given
    User user = new User(1L, "john");
    long attemptId = 5L;
    ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 70, "john", 3500);
    ChallengeAttempt expectedResponse = new ChallengeAttempt(attemptId, user, 50, 70, 3500, true);
    given(challengeService.verifyAttempt(eq(attemptDTO))).willReturn(expectedResponse);

    // When
    MockHttpServletResponse response =
        mvc.perform(
                post("/attemps")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequestAttempt.write(attemptDTO).getJson()))
            .andReturn()
            .getResponse();

    // Then
    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    then(response.getContentAsString())
        .isEqualTo(jsonResultAttempt.write(expectedResponse).getJson());
  }

  @Test
  void postInvalidResult() throws Exception {
    // Given: an attempt with invalid input data
    ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(2000, -70, "john", 1);

    // When
    MockHttpServletResponse response =
        mvc.perform(
                post("/attempts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequestAttempt.write(attemptDTO).getJson()))
            .andReturn()
            .getResponse();
    // Then
    then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void getAttempts() throws Exception {
    //Given

    // When
    MockHttpServletResponse response =
            mvc.perform(
                            post("/attempts/john_doe")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse();
    // Then
    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

}
