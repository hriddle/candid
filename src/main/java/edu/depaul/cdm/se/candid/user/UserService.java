package edu.depaul.cdm.se.candid.user;

import edu.depaul.cdm.se.candid.user.repository.User;
import edu.depaul.cdm.se.candid.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void changeEmail(String id, String newEmail) {
        User user = getUser(id);
        if (user != null) {
            user.getCredentials().setEmail(newEmail);
            userRepository.save(user);
        }
    }

    public void changePassword(String id, String newPassword) {
        User user = getUser(id);
        if (user != null) {
            user.getCredentials().setPassword(newPassword);
            userRepository.save(user);
        }
    }

    public void changeName(String id, String newFirstName, String newLastName) {
        User user = getUser(id);
        if (user != null) {
            user.getProfile().setFirstName(newFirstName);
            user.getProfile().setLastName(newLastName);
            userRepository.save(user);
        }
    }

    public void changeAvatar(String id, String newAvatar) {
        User user = getUser(id);
        if (user != null) {
            user.getProfile().setAvatarLocation(newAvatar);
            userRepository.save(user);
        }
    }

    public void changeAboutMe(String id, String newAboutMe) {
        User user = getUser(id);
        if (user != null) {
            user.getProfile().setAboutMe(newAboutMe);
            userRepository.save(user);
        }
    }

    private User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }
}
