package edu.depaul.cdm.se.candid.feedback;

import edu.depaul.cdm.se.candid.feedback.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Feedback getFeedbackById(String id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    public Feedback markAsRead(String id) {
        Feedback feedback = getFeedbackById(id);
        feedback.setReadByRecipient(true);
        return feedbackRepository.save(feedback);
    }

    public Feedback addReplyToFeedback(String feedbackId, String userId, String text) {
        Feedback feedback = getFeedbackById(feedbackId);
        feedback.getReplies().add(Reply.builder()
            .userId(userId)
            .dateTime(LocalDateTime.now())
            .text(text)
            .build());
        return feedbackRepository.save(feedback);
    }
}
