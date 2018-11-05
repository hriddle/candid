package edu.depaul.cdm.se.candid.feedback;

import edu.depaul.cdm.se.candid.feedback.repository.Feedback;
import edu.depaul.cdm.se.candid.feedback.repository.Question;
import edu.depaul.cdm.se.candid.feedback.repository.FeedbackRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@DataMongoTest
public class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository repository;

    @Before
    public void setUp() throws Exception {
        repository.saveAll(listOfFeedback);

        assertEquals(4, repository.count());
    }

    @Test
    public void findsAll() {
        List<Feedback> feedback = repository.findAll();
        List<String> feedbackIds = feedback.stream().map((Feedback::getId)).collect(Collectors.toList());

        assertEquals(4, feedback.size());
        assertTrue(feedbackIds.contains("1"));
        assertTrue(feedbackIds.contains("2"));
        assertTrue(feedbackIds.contains("3"));
        assertTrue(feedbackIds.contains("4"));
    }

    @Test
    public void findsAllFeedbackBySender() {
        List<Feedback> feedback = repository.findAllBySenderId("jim");

        assertEquals(2, feedback.size());
        assertTrue(feedback.contains(jimToDwight));
        assertTrue(feedback.contains(jimToPam));
    }

    @Test
    public void findsAllFeedbackForRecipient() {
        List<Feedback> feedback = repository.findAllByRecipientId("pam");

        assertEquals(2, feedback.size());
        assertTrue(feedback.contains(jimToPam));
        assertTrue(feedback.contains(dwightToPam));
    }

    private Feedback jimToDwight = Feedback.builder()
        .id("1")
        .senderId("jim")
        .recipientId("dwight")
        .dateWritten(LocalDateTime.of(2018, 9, 1, 0, 0, 0))
        .questions(Collections.singletonList(Question.builder().question("Comment Box").answer("Bears, beets, Battlestar Galactica.").build()))
        .build();

    private Feedback jimToPam = Feedback.builder()
        .id("2")
        .senderId("jim")
        .recipientId("pam")
        .dateWritten(LocalDateTime.of(2018, 9, 4, 0, 0, 0))
        .questions(Collections.singletonList(Question.builder().question("Comment Box").answer("You are everything.").build()))
        .build();

    private Feedback dwightToPam = Feedback.builder()
        .id("3")
        .senderId("dwight")
        .recipientId("pam")
        .dateWritten(LocalDateTime.of(2018, 9, 5, 0, 0, 0))
        .questions(Collections.singletonList(Question.builder().question("Comment Box").answer("You are my friend.").build()))
        .build();

    private Feedback dwightToAngela = Feedback.builder()
        .id("4")
        .senderId("dwight")
        .recipientId("angela")
        .dateWritten(LocalDateTime.of(2018, 9, 7, 0, 0, 0))
        .questions(Collections.singletonList(Question.builder().question("Comment Box").answer("I just want to be friends. Plus a little extra. Also, I love you.").build()))
        .build();

    private List<Feedback> listOfFeedback = asList(jimToDwight, jimToPam, dwightToAngela, dwightToPam);
}
