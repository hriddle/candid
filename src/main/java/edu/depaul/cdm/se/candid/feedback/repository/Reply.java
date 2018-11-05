package edu.depaul.cdm.se.candid.feedback.repository;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class Reply {
    private String userId;
    private LocalDateTime dateTime;
    private String text;
}
