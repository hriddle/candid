package edu.depaul.cdm.se.candid.home;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FeedbackSummaryModel {
    private String id;
    private String dateWritten;
    private String name;
    private String type;
}
