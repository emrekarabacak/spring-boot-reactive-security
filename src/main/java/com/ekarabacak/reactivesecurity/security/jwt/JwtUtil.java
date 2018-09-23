package com.ekarabacak.reactivesecurity.security.jwt;

import com.ekarabacak.reactivesecurity.model.Role;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtUtil {

    private final static String DEFAULT_SECRET = "default-secret-goes-here-as-at-least-256-bits";

    public static Mono<String> generateToken(String username, Set<Role> roles) {


        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("ekarabacak")
                .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .claim("roles", roles.stream().map(role -> role.name()).collect(Collectors.joining(",")))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        try {
            signedJWT.sign(getJWTSigner());
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        return Mono.just(signedJWT.serialize());

    }

    public static JWSSigner getJWTSigner() {
        JWSSigner jwsSigner;
        try {
            jwsSigner = new MACSigner(DEFAULT_SECRET);
        } catch (KeyLengthException e) {
            jwsSigner = null;
        }
        return jwsSigner;
    }

    public static Mono<SignedJWT> verifyToken(String token) {
        try {
            return Mono.just(SignedJWT.parse(token));
        } catch (ParseException e) {
            return Mono.empty();
        }
    }

    public static Mono<? extends Authentication> getAuthtenticationToken(SignedJWT signedJWT) {

        try {
            final String username = signedJWT.getJWTClaimsSet().getSubject();
            final String roles = (String) signedJWT.getJWTClaimsSet().getClaim("roles");

            List<? extends GrantedAuthority> authorities =
                    Stream.of(roles.split(",")).map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());

            return Mono.just(new UsernamePasswordAuthenticationToken(username, null, authorities));
        } catch (ParseException e) {
            return Mono.empty();
        }
    }
}
