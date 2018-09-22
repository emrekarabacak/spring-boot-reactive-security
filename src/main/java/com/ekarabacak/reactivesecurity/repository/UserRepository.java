package com.ekarabacak.reactivesecurity.repository;


import com.ekarabacak.reactivesecurity.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User,String> {
}
