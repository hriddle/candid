package edu.depaul.cdm.se.candid.feedback;

import edu.depaul.cdm.se.candid.feedback.repository.Feedback;
import edu.depaul.cdm.se.candid.feedback.repository.FeedbackRepository;
import edu.depaul.cdm.se.candid.feedback.repository.FeedbackRequest;
import edu.depaul.cdm.se.candid.feedback.repository.FeedbackRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackRequestRepository feedbackRequestRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository, FeedbackRequestRepository feedbackRequestRepository) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackRequestRepository = feedbackRequestRepository;
    }

    public void giveFeedbackToUser(Feedback feedback) {
        feedbackRepository.save(feedback);
    }

    public void requestFeedbackFromUser(FeedbackRequest feedbackRequest) {
        feedbackRequestRepository.save(feedbackRequest);
    }

    public List<Feedback> findAllFeedbackGivenToUser(String recipientId) {
        return feedbackRepository.findAllByRecipientId(recipientId);
    }

    public List<Feedback> findAllFeedbackGivenByUser(String senderId) {
        return feedbackRepository.findAllBySenderId(senderId);
    }

    public List<FeedbackRequest> findAllUnansweredFeedbackRequestsForUser(String respondentId) {
        return feedbackRequestRepository.findAllByRespondentId(respondentId);
    }

}
