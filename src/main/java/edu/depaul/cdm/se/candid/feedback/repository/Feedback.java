package edu.depaul.cdm.se.candid.feedback.repository;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class Feedback {
    @Id
    private String id;
    private String senderId;
    private boolean anonymous;
    private String recipientId;
    private LocalDate dateWritten;
    private List<Question> questions;
    private List<Reply> replies;
    private boolean readByRecipient;
}

