package edu.depaul.cdm.se.candid.feedback.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {
    @Id
    private String id;
    private String initiatorId;
    private String respondentId;
    private String message;
    private FeedbackTemplate template;
}
