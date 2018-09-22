package com.ekarabacak.reactivesecurity.handler;

import com.ekarabacak.reactivesecurity.model.User;
import com.ekarabacak.reactivesecurity.repository.UserRepository;
import com.mongodb.connection.Server;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserHandler {

    private UserRepository userRepository;

    @Autowired
    public UserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<ServerResponse> findAllUsers(ServerRequest request){
        log.info("Finding all users");
        return ServerResponse.ok().body(userRepository.findAll(),User.class);
    }

    public Mono<ServerResponse> findUser(ServerRequest request){
        final String id = request.pathVariable("id");
        log.info("Finding the user {}", id);

        return userRepository.findById(id)
                .flatMap(user -> ServerResponse.ok().syncBody(user))
                .switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> createUser(ServerRequest request){

        final Mono<User> userMono = request.bodyToMono(User.class);
        return userMono
                .map(user -> userRepository.save(user))
                .flatMap(createdUserMono -> ServerResponse.ok().body(createdUserMono,User.class))
                .log()
                .onErrorResume(err -> ServerResponse.badRequest().build());

    }

    public Mono<ServerResponse> updateUser(ServerRequest request){

        final String id = request.pathVariable("id");
        Mono<User> updatedUserMono = request.bodyToMono(User.class);

        return userRepository.findById(id)
                .zipWith(updatedUserMono,(existingUser, updatedUser) -> {
                    existingUser.setPassword(updatedUser.getPassword());
                    return existingUser; })
                .map(updated -> userRepository.save(updated))
                .flatMap(userMono -> ServerResponse.ok().body(userMono,User.class))
                .switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> deleteUser(ServerRequest request){

        final String id = request.pathVariable("id");
        return userRepository.deleteById(id)
                .then(ServerResponse.ok().build());
    }

}
