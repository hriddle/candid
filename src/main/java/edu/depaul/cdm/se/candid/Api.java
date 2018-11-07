package edu.depaul.cdm.se.candid;

import edu.depaul.cdm.se.candid.feedback.FeedbackTemplates;
import edu.depaul.cdm.se.candid.feedback.repository.*;
import edu.depaul.cdm.se.candid.user.repository.User;
import edu.depaul.cdm.se.candid.user.repository.UserCredentials;
import edu.depaul.cdm.se.candid.user.repository.UserProfile;
import edu.depaul.cdm.se.candid.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@RestController
public class Api {

    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackTemplateRepository feedbackTemplateRepository;

    @Autowired
    public Api(UserRepository userRepository, FeedbackRepository feedbackRepository, FeedbackTemplateRepository feedbackTemplateRepository) {
        this.userRepository = userRepository;
        this.feedbackRepository = feedbackRepository;
        this.feedbackTemplateRepository = feedbackTemplateRepository;
    }

    @PostMapping(value = "world")
    public void buildTheWorld() {
        FeedbackTemplate simple = FeedbackTemplates.simple();
        FeedbackTemplate ssc = FeedbackTemplates.custom("Stop/Start/Continue", true, "jim", "What should I STOP doing?", "What should I START doing?", "What should I CONTINUE doing?", "Anything else?");

        User jim = User.builder()
            .id("jim")
            .credentials(UserCredentials.builder().email("jim@dundermifflin.com").password("jim-password").build())
            .profile(UserProfile.builder()
                .firstName("Jim")
                .lastName("Halpert")
                .birthday("October 1")
                .location("Scranton, probably")
                .avatarLocation("https://tinyurl.com/y8yweboy")
                .aboutMe("Pros: Smart, cool, good-looking. Remind you of anybody you know? Cons: Not a hard worker. I can spend all day on a project, and he will finish the same project in half an hour. So that should tell you something. - Michael Scott")
                .build())
            .build();
        User dwight = User.builder()
            .id("dwight")
            .credentials(UserCredentials.builder().email("dwight@dundermifflin.com").password("dwight-password").build())
            .profile(UserProfile.builder().firstName("Dwight").lastName("Schrute").build())
            .build();
        User pam = User.builder()
            .id("pam")
            .credentials(UserCredentials.builder().email("pam@dundermifflin.com").password("pam-password").build())
            .profile(UserProfile.builder().firstName("Pam").lastName("Beesly").build())
            .build();
        User angela = User.builder()
            .id("angela")
            .credentials(UserCredentials.builder().email("angela@dundermifflin.com").password("angela-password").build())
            .profile(UserProfile.builder().firstName("Angela").lastName("Martin").build())
            .build();

        Feedback jimToDwight = Feedback.builder()
            .id("from-jim-to-dwight")
            .senderId("jim")
            .recipientId("dwight")
            .dateWritten(LocalDateTime.of(2018, 9, 1, 9, 0, 0))
            .questions(singletonList(Question.builder().question("Comments").answer("Bears, beets, Battlestar Galactica.").build()))
            .build();
        Feedback pamToJim = Feedback.builder()
            .id("from-pam-to-jim")
            .senderId("pam")
            .recipientId("jim")
            .dateWritten(LocalDateTime.of(2018, 9, 4, 3, 55, 42))
            .questions(asList(
                Question.builder()
                    .question("Are you free for dinner tonight?")
                    .answer("Yes. It's a date.")
                    .build(),
                Question.builder()
                    .question("Do you know when I fell in love with you?")
                    .answer("No, when?")
                    .build()))
            .replies(asList(Reply.builder()
                    .userId("pam")
                    .text("You came up to my desk and said, \"This may sound weird, and there's no reason for me to know this, but the mixed berry yogurt you're about to eat has expired.\"")
                    .dateTime(LocalDateTime.of(2018, 9, 4, 7, 12, 0))
                    .build(),
                Reply.builder()
                    .userId("jim")
                    .text("That was the moment you knew you liked me?")
                    .dateTime(LocalDateTime.of(2018, 9, 4, 7, 19, 0))
                    .build(),
                Reply.builder()
                    .userId("pam")
                    .text("Yup.")
                    .dateTime(LocalDateTime.of(2018, 9, 4, 7, 57, 0))
                    .build(),
                Reply.builder()
                    .userId("jim")
                    .text("Wow. Can we make it a different moment?")
                    .dateTime(LocalDateTime.of(2018, 9, 4, 8, 33, 0))
                    .build(),
                Reply.builder()
                    .userId("pam")
                    .text("Nope.")
                    .dateTime(LocalDateTime.of(2018, 9, 4, 12, 48, 0))
                    .build()
            )).build();
        Feedback dwightToPam = Feedback.builder()
            .id("from-dwight-to-pam")
            .senderId("dwight")
            .recipientId("pam")
            .dateWritten(LocalDateTime.of(2018, 9, 5, 18, 10, 44))
            .questions(singletonList(Question.builder().question("Comments").answer("You are my friend.").build()))
            .build();
        Feedback dwightToAngela = Feedback.builder()
            .id("from-dwight-to-angela")
            .senderId("dwight")
            .recipientId("angela")
            .dateWritten(LocalDateTime.of(2018, 9, 7, 14, 24, 11))
            .questions(singletonList(Question.builder().question("Comments").answer("I just want to be friends. Plus a little extra. Also, I love you.").build()))
            .build();

        feedbackTemplateRepository.saveAll(asList(simple, ssc));
        userRepository.saveAll(asList(jim, dwight, pam, angela));
        feedbackRepository.saveAll(asList(jimToDwight, pamToJim, dwightToAngela, dwightToPam));
    }

    @GetMapping(value = "feedback")
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    @GetMapping(value = "users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(value = "feedback-templates")
    public List<FeedbackTemplate> getAllTemplates() {
        return feedbackTemplateRepository.findAll();
    }
}
