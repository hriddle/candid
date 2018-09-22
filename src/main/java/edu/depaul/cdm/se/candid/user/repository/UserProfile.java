package edu.depaul.cdm.se.candid.user.repository;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class UserProfile {
    private String firstName;
    private String lastName;
    private String aboutMe;
    private String avatarLocation;
}
