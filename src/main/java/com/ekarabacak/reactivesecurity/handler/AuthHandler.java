package com.ekarabacak.reactivesecurity.handler;

import com.ekarabacak.reactivesecurity.exception.InvalidRequestBodyException;
import com.ekarabacak.reactivesecurity.exception.UserNotFoundException;
import com.ekarabacak.reactivesecurity.model.AuthenticationRequest;
import com.ekarabacak.reactivesecurity.model.AuthenticationResponse;
import com.ekarabacak.reactivesecurity.model.User;
import com.ekarabacak.reactivesecurity.repository.UserRepository;
import com.ekarabacak.reactivesecurity.security.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AuthHandler {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public AuthHandler(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<ServerResponse> authenticate(ServerRequest request) {
        final Mono<AuthenticationRequest> authenticationRequestMono = request.bodyToMono(AuthenticationRequest.class);
        return authenticationRequestMono
                .defaultIfEmpty(AuthenticationRequest.invalid())
                .flatMap(this::validateRequest)
                .flatMap(this::checkUser)
                .flatMap(user -> JwtUtil.generateToken(user.getUsername(),user.getRoles()))
                .flatMap(token -> ServerResponse.ok().syncBody(new AuthenticationResponse(token)))
                .onErrorResume(err -> {
                    if (err instanceof UserNotFoundException) {
                        return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
                    } else {
                        return ServerResponse.status(HttpStatus.BAD_REQUEST).build();
                    }
                });

    }

    private Mono<User> checkUser(AuthenticationRequest authenticationRequest) {
        return userRepository.findByUsername(authenticationRequest.getUsername())
                .filter(user -> passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword()))
                .flatMap(Mono::just)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Unauthorized")));
    }

    private Mono<AuthenticationRequest> validateRequest(AuthenticationRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return Mono.error(new InvalidRequestBodyException("Invalid Request Body"));
        }
        return Mono.just(request);
    }
}
