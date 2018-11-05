package edu.depaul.cdm.se.candid.home;

import edu.depaul.cdm.se.candid.feedback.repository.Question;
import edu.depaul.cdm.se.candid.feedback.repository.Reply;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class FeedbackDetailModel {
    private String id;
    private String senderName;
    private boolean anonymous;
    private String dateWritten;
    private List<QuestionModel> questions;
    private List<ReplyModel> replies;
    private boolean readByRecipient;
}
