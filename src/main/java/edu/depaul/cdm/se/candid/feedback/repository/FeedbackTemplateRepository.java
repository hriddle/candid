package edu.depaul.cdm.se.candid.feedback.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackTemplateRepository extends MongoRepository<FeedbackTemplate, String> {
}
