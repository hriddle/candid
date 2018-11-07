package edu.depaul.cdm.se.candid.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendFeedbackModel {
    private String receiverId;
    private String feedback;
    private boolean anonymous;
}
