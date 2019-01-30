package com.abaiyat.triagews.repository;

import com.abaiyat.triagews.model.UserEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, String> {
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    UserEntity findByUserId(String userId);

    @Query(value = "{'addresses.addressId': ?0 }", fields = "{ _id: 0, addresses: {$elemMatch: {addressId: ?0 }} }")
    UserEntity findByAddressId(String addressId);
}
