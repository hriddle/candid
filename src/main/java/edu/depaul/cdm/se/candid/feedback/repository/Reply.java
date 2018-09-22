package edu.depaul.cdm.se.candid.feedback.repository;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Reply {
    private String userId;
    private String text;
    private List<Reply> replies;
}
