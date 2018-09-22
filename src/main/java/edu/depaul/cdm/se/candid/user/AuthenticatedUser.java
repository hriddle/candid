package edu.depaul.cdm.se.candid.user;

import edu.depaul.cdm.se.candid.user.repository.UserProfile;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticatedUser {
    private String id;
    private String email;
    private UserProfile userProfile;
}
