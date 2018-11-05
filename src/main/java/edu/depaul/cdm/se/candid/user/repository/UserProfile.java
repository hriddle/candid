package edu.depaul.cdm.se.candid.user.repository;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder(toBuilder = true)
@Data
public class UserProfile {
    private String firstName;
    private String lastName;
    private String birthday;
    private String location;
    private String aboutMe;
    private String avatarLocation;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
