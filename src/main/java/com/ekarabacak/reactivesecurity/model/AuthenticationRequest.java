package com.ekarabacak.reactivesecurity.model;

import lombok.Data;

@Data

public class AuthenticationRequest {
    private final String username;
    private final String password;

    public static AuthenticationRequest invalid(){
        return new AuthenticationRequest(null,null);
    }
}
