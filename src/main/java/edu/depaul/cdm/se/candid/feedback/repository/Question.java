package edu.depaul.cdm.se.candid.feedback.repository;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Question {
    private String question;
    private String answer;
    private List<Reply> replies;
}
