package com.abaiyat.triagews.repository;

import com.abaiyat.triagews.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {
    UserModel findByEmail(String email);
    UserModel findByUsername(String username);
}
