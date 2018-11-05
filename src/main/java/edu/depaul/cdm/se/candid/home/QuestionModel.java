package edu.depaul.cdm.se.candid.home;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QuestionModel {
    private String question;
    private String answer;
}
