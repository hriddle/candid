package edu.depaul.cdm.se.candid.feedback.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FeedbackRequestRepository extends MongoRepository<FeedbackRequest, String> {

    List<FeedbackRequest> findAllByRespondentId(String respondentId);
    List<FeedbackRequest> findAllByInitiatorId(String initiatorId);
}
