package edu.depaul.cdm.se.candid.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequestModel {
    private String id;
    private String requestDate;
    private String name;
    private String message;
    private boolean incomplete;
}
