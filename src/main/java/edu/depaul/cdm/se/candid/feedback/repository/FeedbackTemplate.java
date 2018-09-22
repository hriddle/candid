package edu.depaul.cdm.se.candid.feedback.repository;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Builder
@Data
public class FeedbackTemplate {
    @Id
    private String id;
    private String name;
    private boolean personal;
    private String createdBy;
    private List<Question> questions;
}
