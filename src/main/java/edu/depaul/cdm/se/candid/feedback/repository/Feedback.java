package edu.depaul.cdm.se.candid.feedback.repository;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Singular;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class Feedback {
    @Id
    private String id;
    private String senderId;
    private boolean anonymous;
    private String recipientId;
    private LocalDateTime dateWritten;
    private String templateName;
    @Singular
    private List<Question> questions;
    @Singular
    private List<Reply> replies;
    private boolean readByRecipient;
}

