package edu.depaul.cdm.se.candid.user.login;

public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException() {
        super("Your email or password were incorrect.");
    }
}
