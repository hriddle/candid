package edu.depaul.cdm.se.candid.home;

import edu.depaul.cdm.se.candid.feedback.FeedbackService;
import edu.depaul.cdm.se.candid.feedback.repository.Feedback;
import edu.depaul.cdm.se.candid.feedback.repository.FeedbackRequest;
import edu.depaul.cdm.se.candid.feedback.repository.FeedbackRequestRepository;
import edu.depaul.cdm.se.candid.feedback.repository.Question;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final UserService userService;
    private final FeedbackService feedbackService;
    private final FeedbackRequestRepository feedbackRequestRepository;

    private String loggedInUserId = "jim";

    @Autowired
    public HomeController(UserService userService, FeedbackService feedbackService, FeedbackRequestRepository feedbackRequestRepository) {
        this.userService = userService;
        this.feedbackService = feedbackService;
        this.feedbackRequestRepository = feedbackRequestRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        AuthenticatedUser user = getAuthenticatedUser(loggedInUserId);
        if (user != null) {
            List<FeedbackSummaryModel> received = feedbackService.findAllFeedbackGivenToUser(user.getId())
                .stream().map(feedback -> FeedbackSummaryModel.builder()
                    .id(feedback.getId())
                    .dateWritten(formatFeedbackDate(feedback.getDateWritten()))
                    .name(getUserFullName(feedback.getSenderId()))
                    .type(feedback.getTemplateName() != null ? feedback.getTemplateName() : "Simple")
                    .unread(!feedback.isReadByRecipient())
                    .build()
                ).collect(Collectors.toList());
            List<FeedbackSummaryModel> given = feedbackService.findAllFeedbackGivenByUser(user.getId())
                .stream().map(feedback -> FeedbackSummaryModel.builder()
                    .id(feedback.getId())
                    .dateWritten(formatFeedbackDate(feedback.getDateWritten()))
                    .name(getUserFullName(feedback.getRecipientId()))
                    .type(feedback.getTemplateName() != null ? feedback.getTemplateName() : "Simple")
                    .unread(!feedback.isReadByRecipient())
                    .build()
                ).collect(Collectors.toList());

            model.addAttribute("user", user);
            model.addAttribute("received", received);
            model.addAttribute("given", given);
            model.addAttribute("updatedProfile", user.getUserProfile());
            return UI.INDEX;
        }
        return UI.LOGIN;
    }

    @GetMapping("feedback/{id}")
    public String getFeedbackDetail(@PathVariable String id, Model model) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        if (feedback.getRecipientId().equals(loggedInUserId)) {
            feedbackService.markAsRead(id);
        }
        model.addAttribute("feedback", toFeedbackDetailModel(feedback));
        model.addAttribute("replyModel", ReplyModel.builder().build());
        return UI.FEEDBACK;
    }

    @PostMapping("feedback/{feedbackId}/replies")
    public String replyToFeedback(@PathVariable String feedbackId, @ModelAttribute ReplyModel replyModel) {
        feedbackService.addReplyToFeedback(feedbackId, loggedInUserId, replyModel.getText());
        return redirectTo("/feedback/" + feedbackId); //getFeedbackDetail(feedbackId, model);
    }

    @PostMapping("users/{userId}/profile")
    public String updateUserProfile(@PathVariable String userId, @ModelAttribute UserProfile updatedProfile) {
        User user = userService.getUser(userId);
        if (updatedProfile.getAvatarLocation() != null &&
            !user.getProfile().getAvatarLocation().equals(updatedProfile.getAvatarLocation())) {
            userService.changeAvatar(userId, updatedProfile.getAvatarLocation());
        }
        if (updatedProfile.getBirthday() != null &&
            !user.getProfile().getBirthday().equals(updatedProfile.getBirthday())) {
            userService.changeBirthday(userId, updatedProfile.getBirthday());
        }
        if (updatedProfile.getLocation() != null &&
            !user.getProfile().getLocation().equals(updatedProfile.getLocation())) {
            userService.changeLocation(userId, updatedProfile.getLocation());
        }
        if (updatedProfile.getAboutMe() != null &&
            !user.getProfile().getAboutMe().equals(updatedProfile.getAboutMe())) {
            userService.changeAboutMe(userId, updatedProfile.getAboutMe());
        }
        return redirectTo("/");
    }

    @GetMapping("/give-feedback")
    public String giveFeedback(Model model) {
        Map<String, String> userMap = userService.findAll().stream()
            .filter(user -> !user.getId().equals(loggedInUserId))
            .collect(Collectors.toMap(User::getId, user -> user.getProfile().getFullName()));
        model.addAttribute("userMap", userMap);
        model.addAttribute("feedback", new SendFeedbackModel());

        return UI.SEND_FEEDBACK;
    }

    @GetMapping("/request-feedback")
    public String requestFeedback(Model model) {
        Map<String, String> userMap = userService.findAll().stream()
            .filter(user -> !user.getId().equals(loggedInUserId))
            .collect(Collectors.toMap(User::getId, user -> user.getProfile().getFullName()));
        model.addAttribute("userMap", userMap);
        model.addAttribute("feedback", new FeedbackRequest());

        return UI.REQUEST_FEEDBACK;
    }

    @GetMapping("/feedback-requests")
    public String getFeedbackRequests(Model model) {
        Map<String, String> userMap = userService.findAll().stream()
            .filter(user -> !user.getId().equals(loggedInUserId))
            .collect(Collectors.toMap(User::getId, user -> user.getProfile().getFullName()));

        List<FeedbackRequestModel> feedbackRequests = feedbackRequestRepository.findAllByRespondentId(loggedInUserId).stream()
            .map(f -> FeedbackRequestModel.builder()
                    .id(f.getId())
                    .name(userMap.get(f.getInitiatorId()))
                    .requestDate(formatFeedbackDate(f.getRequestDate()))
                    .message(f.getMessage())
                    .incomplete(!f.isCompleted())
                    .build()
            ).collect(Collectors.toList());
        List<FeedbackRequestModel> requestedFeedback = feedbackRequestRepository.findAllByInitiatorId(loggedInUserId).stream()
            .map(f -> FeedbackRequestModel.builder()
                .id(f.getId())
                .name(userMap.get(f.getRespondentId()))
                .requestDate(formatFeedbackDate(f.getRequestDate()))
                .message(f.getMessage())
                .incomplete(!f.isCompleted())
                .build()
            ).collect(Collectors.toList());

        AuthenticatedUser user = getAuthenticatedUser(loggedInUserId);
        model.addAttribute("user", user);
        model.addAttribute("updatedProfile", user.getUserProfile());
        model.addAttribute("feedbackRequests", feedbackRequests);
        model.addAttribute("requestedFeedback", requestedFeedback);
        return UI.FEEDBACK_REQUESTS;
    }

    @PostMapping("/feedback")
    public String sendFeedback(@ModelAttribute("feedback") SendFeedbackModel sendFeedbackModel) {

        feedbackService.giveFeedbackToUser(Feedback.builder()
            .senderId(loggedInUserId)
            .recipientId(sendFeedbackModel.getReceiverId())
            .anonymous(sendFeedbackModel.isAnonymous())
            .dateWritten(LocalDateTime.now())
            .readByRecipient(false)
            .templateName("Simple")
            .question(Question.builder().question("Comments").answer(sendFeedbackModel.getFeedback()).build())
            .replies(new ArrayList<>())
            .build());

        return redirectTo("/");
    }

    @PostMapping("/feedbackRequest")
    public String feedbackRequest(@ModelAttribute("feedback") FeedbackRequest feedbackRequest) {
        feedbackRequest.setInitiatorId(loggedInUserId);
        feedbackRequest.setRequestDate(LocalDateTime.now());
        feedbackRequest.setCompleted(false);
        feedbackRequestRepository.save(feedbackRequest);
        return redirectTo("/");
    }

    private String redirectTo(String path) {
        return "redirect:" + path;
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
            .senderName(getSenderName(feedback))
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

    private String getSenderName(Feedback feedback) {
        if (feedback.isAnonymous()) {
            return feedback.getSenderId().equals(loggedInUserId) ? "You (Anonymous)" : "Anonymous";
        } else {
            return getUserFullName(feedback.getSenderId());
        }
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
            return "Today, " + date.getHour() + ":" + date.getMinute();
        } else if (isYesterday) {
            return "Yesterday, " + date.getHour() + ":" + date.getMinute();
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
