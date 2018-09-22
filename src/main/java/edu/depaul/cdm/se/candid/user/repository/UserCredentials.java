package edu.depaul.cdm.se.candid.user.repository;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

@Builder(toBuilder = true)
@Data
public class UserCredentials {
    @Indexed(unique = true)
    private String email;
    private String password;
}
