package edu.depaul.cdm.se.candid.user.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Before
    public void setUp() throws Exception {
        repository.saveAll(users);
        assertEquals(4, repository.count());
    }

    @Test
    public void findsDistinctByEmailAndPassword() {
        User user = repository.findDistinctByCredentials_EmailAndCredentials_Password("pam@dundermifflin.com", "pam-password");

        assertEquals(pam, user);
    }

    @Test
    public void doesNotFindByEmailAndPassword() {
        User wrongPassword = repository.findDistinctByCredentials_EmailAndCredentials_Password("dwight@dundermifflin.com", "not-dwight-password");
        assertNull(wrongPassword);

        User wrongEmail = repository.findDistinctByCredentials_EmailAndCredentials_Password("notjim@dundermifflin.com", "jim-password");
        assertNull(wrongEmail);

        User wrongEmailAndPassword = repository.findDistinctByCredentials_EmailAndCredentials_Password("notjim@dundermifflin.com", "not-jim-password");
        assertNull(wrongEmailAndPassword);
    }

    private User jim = User.builder()
        .id("jim")
        .credentials(UserCredentials.builder().email("jim@dundermifflin.com").password("jim-password").build())
        .profile(UserProfile.builder().firstName("Jim").lastName("Halpert").build())
        .build();

    private User dwight = User.builder()
        .id("dwight")
        .credentials(UserCredentials.builder().email("dwight@dundermifflin.com").password("dwight-password").build())
        .profile(UserProfile.builder().firstName("Dwight").lastName("Schrute").build())
        .build();

    private User pam = User.builder()
        .id("pam")
        .credentials(UserCredentials.builder().email("pam@dundermifflin.com").password("pam-password").build())
        .profile(UserProfile.builder().firstName("Pam").lastName("Beesly").build())
        .build();

    private User angela = User.builder()
        .id("angela")
        .credentials(UserCredentials.builder().email("angela@dundermifflin.com").password("angela-password").build())
        .profile(UserProfile.builder().firstName("Angela").lastName("Martin").build())
        .build();

    private List<User> users = asList(jim, dwight, pam, angela);
}