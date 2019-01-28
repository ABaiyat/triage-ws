package com.abaiyat.triagews.repository;

import com.abaiyat.triagews.model.UserModel;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserModel, String> {
    UserModel findByEmail(String email);
    UserModel findByUsername(String username);
    UserModel findByUserId(String userId);
}
