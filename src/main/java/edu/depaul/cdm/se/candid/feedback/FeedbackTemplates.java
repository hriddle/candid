package edu.depaul.cdm.se.candid.feedback;

import edu.depaul.cdm.se.candid.feedback.repository.Question;
import edu.depaul.cdm.se.candid.feedback.repository.FeedbackTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackTemplates {

    public static FeedbackTemplate simple() {
        return FeedbackTemplate.builder()
            .name("Simple")
            .questions(questions("Comments"))
            .build();
    }

    public static FeedbackTemplate custom(String name, boolean personal, String createdBy, String... questions) {
        return FeedbackTemplate.builder()
            .name(name)
            .personal(personal)
            .createdBy(createdBy)
            .questions(questions(questions))
            .build();
    }

    private static List<Question> questions(String... questions) {
        return Arrays.stream(questions)
                .map(question -> Question.builder().question(question).build())
                .collect(Collectors.toList());
    }
}
