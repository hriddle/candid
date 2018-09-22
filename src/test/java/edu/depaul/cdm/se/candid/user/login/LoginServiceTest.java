package edu.depaul.cdm.se.candid.user.login;

import edu.depaul.cdm.se.candid.user.AuthenticatedUser;
import edu.depaul.cdm.se.candid.user.repository.User;
import edu.depaul.cdm.se.candid.user.repository.UserCredentials;
import edu.depaul.cdm.se.candid.user.repository.UserProfile;
import edu.depaul.cdm.se.candid.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LoginServiceTest {

    private LoginService service;
    private UserRepository mockUserRepository = mock(UserRepository.class);
    private PasswordEncoder mockPasswordEncoder = mock(PasswordEncoder.class);

    private String validEmail = "user@example.com";
    private String correctPassword = "totally-correct-password";
    private String incorrectPassword = "this-password-is-wrong";

    private User fakeUser = User.builder()
        .id("mongo-id")
        .credentials(
            UserCredentials.builder().email(validEmail).password(correctPassword).build()
        ).profile(
            UserProfile.builder().firstName("User").lastName("Test").build()
        ).build();

    @Before
    public void setUp() {

        when(mockUserRepository.findDistinctByCredentials_EmailAndCredentials_Password(validEmail, correctPassword)).thenReturn(fakeUser);
        when(mockUserRepository.findDistinctByCredentials_EmailAndCredentials_Password(validEmail, incorrectPassword)).thenReturn(null);

        when(mockPasswordEncoder.encode(correctPassword)).thenReturn(correctPassword);
        when(mockPasswordEncoder.encode(incorrectPassword)).thenReturn(incorrectPassword);

        service = new LoginService(mockUserRepository, mockPasswordEncoder);
    }

    @Test
    public void login_shouldReturnAuthorizedUserWhenSuccessful() throws Exception {
        LoginAttempt loginAttempt = LoginAttempt.builder()
            .email(validEmail)
            .password(correctPassword).build();

        AuthenticatedUser actual = service.login(loginAttempt);
        AuthenticatedUser expected = AuthenticatedUser.builder()
            .id(fakeUser.getId())
            .email(fakeUser.getCredentials().getEmail())
            .userProfile(fakeUser.getProfile())
            .build();
        verify(mockUserRepository).findDistinctByCredentials_EmailAndCredentials_Password(validEmail, correctPassword);
        assertEquals(expected, actual);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void login_shouldThrowInvalidCredentialsExceptionWithIncorrectPassword() throws Exception {
        LoginAttempt loginAttempt = LoginAttempt.builder()
            .email(validEmail)
            .password(incorrectPassword)
            .build();

        try {
            service.login(loginAttempt);
        } catch (InvalidCredentialsException e) {
            verify(mockUserRepository).findDistinctByCredentials_EmailAndCredentials_Password(validEmail, incorrectPassword);
            throw e;
        }
    }
}