package edu.depaul.cdm.se.candid.user.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class User {
    @Id
    private String id;
    private UserCredentials credentials;
    private UserProfile profile;
}
