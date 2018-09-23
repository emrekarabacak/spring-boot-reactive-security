package com.ekarabacak.reactivesecurity.security;

import com.ekarabacak.reactivesecurity.security.jwt.JwtUtil;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        return Mono.justOrEmpty((String)authentication.getCredentials())
                .flatMap(JwtUtil::verifyToken)
                .flatMap(JwtUtil::getAuthtenticationToken);
    }
}
