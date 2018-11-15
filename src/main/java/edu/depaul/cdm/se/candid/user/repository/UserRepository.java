package edu.depaul.cdm.se.candid.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findDistinctByCredentials_EmailAndCredentials_Password(String email, String password);
    User findByCredentials_Email(String email);
}
