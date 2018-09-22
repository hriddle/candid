package edu.depaul.cdm.se.candid.user.login;

import edu.depaul.cdm.se.candid.user.AuthenticatedUser;
import edu.depaul.cdm.se.candid.user.repository.User;
import edu.depaul.cdm.se.candid.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticatedUser login(LoginAttempt loginAttempt) throws InvalidCredentialsException {
        User user = userRepository.findDistinctByCredentials_EmailAndCredentials_Password(loginAttempt.getEmail(), encode(loginAttempt.getPassword()));
        if (user == null) throw new InvalidCredentialsException();
        return AuthenticatedUser.builder()
                .id(user.getId())
                .email(user.getCredentials().getEmail())
                .userProfile(user.getProfile())
                .build();
    }

    private String encode(String value) {
        return passwordEncoder.encode(value);
    }
}
