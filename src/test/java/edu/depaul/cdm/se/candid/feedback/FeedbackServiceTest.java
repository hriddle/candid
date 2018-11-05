package edu.depaul.cdm.se.candid.feedback;

import edu.depaul.cdm.se.candid.feedback.repository.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FeedbackServiceTest {

    private FeedbackService service;
    private FeedbackRepository mockFeedbackRepository = mock(FeedbackRepository.class);
    private FeedbackRequestRepository mockFeedbackRequestRepository = mock(FeedbackRequestRepository.class);

    @Before
    public void setUp() {
        service = new FeedbackService(mockFeedbackRepository, mockFeedbackRequestRepository);
    }

    @Test
    public void giveFeedback_savesFeedbackInRepository() {
        List<Question> questions = FeedbackTemplates.simple().getQuestions();

        questions.get(0).setAnswer("You're the bee's knees!");

        Feedback feedback = Feedback.builder()
            .dateWritten(LocalDateTime.now())
            .anonymous(false)
            .questions(questions)
            .build();

        service.giveFeedbackToUser(feedback);
        verify(mockFeedbackRepository).save(feedback);
    }
}
