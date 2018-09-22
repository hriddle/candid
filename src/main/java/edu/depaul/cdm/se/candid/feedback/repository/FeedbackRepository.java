package edu.depaul.cdm.se.candid.feedback.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {

    List<Feedback> findAllByRecipientId(String recipientId);

    List<Feedback> findAllBySenderId(String senderId);
}
