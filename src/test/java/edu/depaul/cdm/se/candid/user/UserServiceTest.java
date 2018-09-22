package edu.depaul.cdm.se.candid.user;

import edu.depaul.cdm.se.candid.user.repository.User;
import edu.depaul.cdm.se.candid.user.repository.UserCredentials;
import edu.depaul.cdm.se.candid.user.repository.UserProfile;
import edu.depaul.cdm.se.candid.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserRepository mockUserRepository = mock(UserRepository.class);
    private UserService userService = new UserService(mockUserRepository);

    private User pam = User.builder()
        .id("pam")
        .credentials(UserCredentials.builder().email("pam@dundermifflin.com").password("pam-password").build())
        .profile(UserProfile.builder().firstName("Pam").lastName("Beesly").build())
        .build();

    @Before
    public void setUp() throws Exception {
        when(mockUserRepository.findById("pam")).thenReturn(Optional.of(pam));
    }

    @Test
    public void changeEmail() {
        String newEmail = "pamhalpert@dundermifflin.com";
        userService.changeEmail("pam", newEmail);

        User newPam = User.builder()
            .id("pam")
            .credentials(UserCredentials.builder().email(newEmail).password("pam-password").build())
            .profile(UserProfile.builder().firstName("Pam").lastName("Beesly").build())
            .build();
        verify(mockUserRepository).findById("pam");
        verify(mockUserRepository).save(newPam);
    }

    @Test
    public void changePassword() {
        String newPassword = "new-password";
        userService.changePassword("pam", newPassword);

        User newPam = User.builder()
            .id("pam")
            .credentials(UserCredentials.builder().email("pam@dundermifflin.com").password(newPassword).build())
            .profile(UserProfile.builder().firstName("Pam").lastName("Beesly").build())
            .build();
        verify(mockUserRepository).findById("pam");
        verify(mockUserRepository).save(newPam);
    }

    @Test
    public void changeName() {
        String newFirstName = "Pam";
        String newLastName = "Halpert";
        userService.changeName("pam", newFirstName, newLastName);

        User newPam = User.builder()
            .id("pam")
            .credentials(UserCredentials.builder().email("pam@dundermifflin.com").password("pam-password").build())
            .profile(UserProfile.builder().firstName("Pam").lastName("Halpert").build())
            .build();

        verify(mockUserRepository).findById("pam");
        verify(mockUserRepository).save(newPam);
    }

    @Test
    public void changeAvatar() {
        String newAvatar = "abc";
        userService.changeAvatar("pam", newAvatar);

        User newPam = User.builder()
            .id("pam")
            .credentials(UserCredentials.builder().email("pam@dundermifflin.com").password("pam-password").build())
            .profile(UserProfile.builder().firstName("Pam").lastName("Beesly").avatarLocation(newAvatar).build())
            .build();

        verify(mockUserRepository).findById("pam");
        verify(mockUserRepository).save(newPam);
    }

    @Test
    public void changeAboutMe() {
        String newAboutMe = "I <3 Jim";
        userService.changeAboutMe("pam", newAboutMe);

        User newPam = User.builder()
            .id("pam")
            .credentials(UserCredentials.builder().email("pam@dundermifflin.com").password("pam-password").build())
            .profile(UserProfile.builder().firstName("Pam").lastName("Beesly").aboutMe(newAboutMe).build())
            .build();

        verify(mockUserRepository).findById("pam");
        verify(mockUserRepository).save(newPam);
    }
}