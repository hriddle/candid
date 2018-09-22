package edu.depaul.cdm.se.candid.feedback;

import edu.depaul.cdm.se.candid.feedback.repository.FeedbackTemplate;
import edu.depaul.cdm.se.candid.feedback.repository.Question;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FeedbackTemplatesTest {

    @Test
    public void customTemplate_buildsTemplateWithAllQuestions() {
        FeedbackTemplate template = FeedbackTemplates.custom("Custom Template", false, null, "What should I stop doing?", "Cats", "Purple?");

        assertEquals(Arrays.asList(
                Question.builder().question("What should I stop doing?").build(),
                Question.builder().question("Cats").build(),
                Question.builder().question("Purple?").build()
        ), template.getQuestions());

        assertEquals("Custom Template", template.getName());
        assertFalse(template.isPersonal());
        assertNull(template.getCreatedBy());
    }
}