package edu.depaul.cdm.se.candid.user.login;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginAttempt {
    private String email;
    private String password;
}
