package edu.depaul.cdm.se.candid.user.login;

import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

@Service
public class PasswordEncoder {

    public String encode(String value) {
        return Base64Utils.encodeToString(value.getBytes());
    }
}
