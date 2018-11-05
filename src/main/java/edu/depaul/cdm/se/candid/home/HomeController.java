package edu.depaul.cdm.se.candid.home;

import edu.depaul.cdm.se.candid.feedback.FeedbackService;
import edu.depaul.cdm.se.candid.feedback.repository.Feedback;
import edu.depaul.cdm.se.candid.user.AuthenticatedUser;
import edu.depaul.cdm.se.candid.user.UserService;
import edu.depaul.cdm.se.candid.user.repository.User;
import edu.depaul.cdm.se.candid.user.repository.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final UserService userService;
    private final FeedbackService feedbackService;

    private String loggedInUserId = "jim";

    @Autowired
    public HomeController(UserService userService, FeedbackService feedbackService) {
        this.userService = userService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/")
    String index(Model model) {
        AuthenticatedUser user = getAuthenticatedUser(loggedInUserId);
        if (user != null) {
            model.addAttribute("user", user);

            List<FeedbackSummaryModel> received = feedbackService.findAllFeedbackGivenToUser(user.getId())
                .stream().map(feedback -> FeedbackSummaryModel.builder()
                    .id(feedback.getId())
                    .dateWritten(formatFeedbackDate(feedback.getDateWritten()))
                    .name(getUserFullName(feedback.getSenderId()))
                    .type(feedback.getTemplateName() != null ? feedback.getTemplateName() : "Simple")
                    .build()
                ).collect(Collectors.toList());
            List<FeedbackSummaryModel> given = feedbackService.findAllFeedbackGivenByUser(user.getId())
                .stream().map(feedback -> FeedbackSummaryModel.builder()
                    .id(feedback.getId())
                    .dateWritten(formatFeedbackDate(feedback.getDateWritten()))
                    .name(getUserFullName(feedback.getRecipientId()))
                    .type(feedback.getTemplateName() != null ? feedback.getTemplateName() : "Simple")
                    .build()
                ).collect(Collectors.toList());
            model.addAttribute("received", received);
            model.addAttribute("given", given);
            return UI.INDEX;
        }
        return UI.LOGIN;
    }

    @GetMapping("feedback/{id}")
    public String getFeedbackDetail(@PathVariable String id, Model model) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        model.addAttribute("feedback", toFeedbackDetailModel(feedback));
        model.addAttribute("replyModel", ReplyModel.builder().build());
        return UI.FEEDBACK;
    }

    @PostMapping("feedback/{feedbackId}/replies")
    public String replyToFeedback(@PathVariable String feedbackId, @ModelAttribute ReplyModel replyModel, Model model) {
        feedbackService.addReplyToFeedback(feedbackId, loggedInUserId, replyModel.getText());
        return "redirect:/feedback/" + feedbackId; //getFeedbackDetail(feedbackId, model);
    }

    private AuthenticatedUser getAuthenticatedUser(String id) {
        User user = userService.getUser(id);
        if (user != null) {
            return AuthenticatedUser.builder()
                .id(user.getId())
                .email(user.getCredentials().getEmail())
                .userProfile(user.getProfile())
                .build();
        }
        return null;
    }

    // TODO pull out into helpers

    private FeedbackDetailModel toFeedbackDetailModel(Feedback feedback) {
        return FeedbackDetailModel.builder()
            .id(feedback.getId())
            .anonymous(feedback.isAnonymous())
            .senderName(feedback.isAnonymous() ? "Anonymous" : getUserFullName(feedback.getSenderId()))
            .dateWritten(formatFeedbackDate(feedback.getDateWritten()))
            .questions(feedback.getQuestions().stream().map(q ->
                QuestionModel.builder()
                    .question(q.getQuestion())
                    .answer(q.getAnswer())
                    .build()
            ).collect(Collectors.toList()))
            .replies(feedback.getReplies().stream().map(r ->
                ReplyModel.builder()
                    .senderId(r.getUserId())
                    .name(r.getUserId().equals(loggedInUserId) ? "You" : getUserFullName(r.getUserId()))
                    .date(formatReplyDate(r.getDateTime()))
                    .text(r.getText())
                    .build()
            ).collect(Collectors.toList()))
            .build();
    }

    private String formatFeedbackDate(LocalDateTime date) {
        if (date == null) return "";
        String monthAndDay = "MMMM d";
        String year = ", yyyy";

        LocalDate today = LocalDate.now();

        boolean isSameYear = date.getYear() == today.getYear();
        boolean isToday = date.getYear() == today.getYear() && date.getMonthValue() == today.getMonthValue() && date.getDayOfMonth() == today.getDayOfMonth();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        boolean isYesterday = date.getYear() == yesterday.getYear() && date.getMonthValue() == yesterday.getMonthValue() && date.getDayOfMonth() == yesterday.getDayOfMonth();

        String pattern = monthAndDay + year;
        if (isToday) {
            pattern = "Today, " + date.getHour() + ":" + date.getMinute();
        } else if (isYesterday) {
            pattern = "Yesterday, " + date.getHour() + ":" + date.getMinute();
        } else if (isSameYear) {
            pattern = monthAndDay;
        }

        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    // TODO pull out into helper
    private String formatReplyDate(LocalDateTime date) {
        if (date == null) return "";
        String monthAndDay = "MMMM d";
        String year = ", yyyy";

        LocalDate today = LocalDate.now();

        boolean isSameYear = date.getYear() == today.getYear();
        boolean isToday = date.getYear() == today.getYear() && date.getMonthValue() == today.getMonthValue() && date.getDayOfMonth() == today.getDayOfMonth();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        boolean isYesterday = date.getYear() == yesterday.getYear() && date.getMonthValue() == yesterday.getMonthValue() && date.getDayOfMonth() == yesterday.getDayOfMonth();

        String time = date.getHour() + ":" + date.getMinute();

        String pattern = monthAndDay + year;
        if (isToday || isYesterday) {
            pattern = time;
        } else if (isSameYear) {
            pattern = monthAndDay + ", " + time;
        }

        String prefix = isToday ? "Today, " : isYesterday ? "Yesterday, " : "";

        return prefix + date.format(DateTimeFormatter.ofPattern(pattern));
    }

    private String getUserFullName(String id) {
        UserProfile profile = userService.getUser(id).getProfile();
        return profile.getFirstName() + " " + profile.getLastName();
    }
}
